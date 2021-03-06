// Copyright 2009-2020 Information & Computational Sciences, JHI. All rights
// reserved. Use is subject to the accompanying licence terms.

package jhi.flapjack.io.brapi;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.zip.*;
import javax.xml.bind.*;

import jhi.flapjack.gui.*;

import jhi.brapi.api.*;
import jhi.brapi.api.authentication.*;
import jhi.brapi.api.core.serverinfo.*;
import jhi.brapi.api.core.studies.*;
import jhi.brapi.api.genotyping.callsets.*;
import jhi.brapi.api.genotyping.genomemaps.*;
import jhi.brapi.api.genotyping.variantsets.*;
import jhi.brapi.client.*;

import retrofit2.Response;
import scri.commons.gui.*;

public class BrapiClient
{
	private RetrofitServiceGenerator generator;
	private RetrofitService service;
	private String baseURL;

	// The resource selected by the user for use
	private XmlResource resource;

	private String username, password;
	private String mapID, studyID, variantSetID, variantSetName;
	private long totalMarkers, totalLines;
	private String ioMissingData, ioHeteroSeparator;

	private CallsUtils callsUtils;

	private AsyncChecker.AsyncStatus status = AsyncChecker.AsyncStatus.PENDING;

	private volatile boolean isOk = true;

	// Used for matrix/json streaming to track the objects we've received
	private HashMap<String,String> markers, lines;
	private long alleles = 0;

	public void initService()
	{
		baseURL = resource.getUrl();
		baseURL = baseURL.endsWith("/") ? baseURL : baseURL + "/";

		generator = new RetrofitServiceGenerator(baseURL, resource.getCertificate());
		service = generator.generate(null);
	}

	private String enc(String str)
	{
		try
		{
			return URLEncoder.encode(str, "UTF-8");
		}
		catch (UnsupportedEncodingException e)
		{
			return str;
		}
	}

	public void getServerInfo()
		throws Exception
	{
		BrapiServerInfo serverInfo = new BrapiServerInfo();

		Response<BrapiBaseResource<BrapiServerInfo>> response = service.getServerInfo(null)
			.execute();

		if (response.isSuccessful())
		{
			BrapiBaseResource<BrapiServerInfo> serverInfoResponse = response.body();

			serverInfo = serverInfoResponse.getResult();
		}
		else
		{
			String errorMessage = ErrorHandler.getMessage(generator, response);

			throw new Exception(errorMessage);
		}

		callsUtils = new CallsUtils(serverInfo.getCalls());
	}

	public boolean validateCalls()
	{
		if (callsUtils.validate() == false)
		{
			TaskDialog.errorWithLog("The selected BrAPI service does not appear to support the required functionality "
				+ "for use by Flapjack (" + callsUtils.getExceptionMsg() + ").");

			return false;
		}

		return true;
	}

	public boolean hasToken()
	{
		return callsUtils.hasToken();
	}

	public boolean hasMaps()
	{
		return callsUtils.hasMaps();
	}

	public boolean hasMapsMapDbId()
	{
		return callsUtils.hasMapsMapDbId();
	}

	public boolean hasStudiesSearch()
	{
		return callsUtils.hasStudiesSearch();
	}

	public boolean doAuthentication()
		throws Exception
	{
		if (username == null && password == null)
			return false;

		BrapiTokenLoginPost tokenPost = new BrapiTokenLoginPost(username, password, "password", "flapjack");

		Response<BrapiSessionToken> response = service.getAuthToken(tokenPost).execute();

		if (response.isSuccessful())
		{
			BrapiSessionToken token = response.body();

			if (token == null)
				return false;

			service = generator.generate(token.getAccess_token());
			return true;
		}
		else
		{
			return false;
		}
	}

	// Returns a list of available maps
	public List<GenomeMap> getMaps()
		throws Exception
	{
		List<GenomeMap> mapList = new ArrayList<>();
		Pager pager = new Pager();

		while (pager.isPaging())
		{
			Response<BrapiListResource<GenomeMap>> response = service.getMaps(null, null, null, null, null, null, null, pager.getPageSize(), pager.getPage())
				.execute();

			if (response.isSuccessful())
			{
				BrapiListResource<GenomeMap> maps = response.body();

				mapList.addAll(maps.data());
				pager.paginate(maps.getMetadata());
			}
			else
			{
				String errorMessage = ErrorHandler.getMessage(generator, response);

				throw new Exception(errorMessage);
			}
		}

		return mapList;
	}

	// Returns the details (markers, chromosomes, positions) for a given map
	public List<MarkerPosition> getMapMarkerData()
		throws Exception
	{
		List<MarkerPosition> markerPositionList = new ArrayList<>();
		Pager pager = new Pager();

		while (pager.isPaging())
		{
			Response<BrapiListResource<MarkerPosition>> response = service.getMarkerPositions(enc(mapID), null, null, null, null, pager.getPageSize(), pager.getPage())
				.execute();

			if (response.isSuccessful())
			{
				BrapiListResource<MarkerPosition> markerPositions = response.body();

				markerPositionList.addAll(markerPositions.data());
				pager.paginate(markerPositions.getMetadata());
			}
			else
			{
				String errorMessage = ErrorHandler.getMessage(generator, response);

				throw new Exception(errorMessage);
			}

		}

		return markerPositionList;
	}

	// Returns a list of available studies
	public List<Study> getStudies()
		throws Exception
	{
		List<Study> studiesList = new ArrayList<>();
		Pager pager = new Pager();

		while (pager.isPaging())
		{
			Response<BrapiListResource<Study>> response = service.getStudies(null, "genotype", null, null,
				null, null, null, null, null, null, null, null, null, null,
				null,  pager.getPageSize(), pager.getPage())
				.execute();

			if (response.isSuccessful())
			{
				BrapiListResource<Study> studies = response.body();

				studiesList.addAll(studies.data());
				pager.paginate(studies.getMetadata());
			}
			else
			{
				String errorMessage = ErrorHandler.getMessage(generator, response);

				throw new Exception(errorMessage);
			}
		}

		return studiesList;
	}

	public List<Study> getStudiesByPost()
		throws Exception
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public List<CallSet> getCallsets()
		throws Exception
	{
		List<CallSet> callsetList = new ArrayList<>();
		Pager pager = new Pager();

		while (pager.isPaging())
		{
			Response<BrapiListResource<CallSet>> response = service.getCallSets(null, null, studyID, null, null, pager.getPageSize(), pager.getPage())
				.execute();

			if (response.isSuccessful())
			{
				BrapiListResource<CallSet> callset = response.body();

				callsetList.addAll(callset.data());
				pager.paginate(callset.getMetadata());
			}
			else
			{
				String errorMessage = ErrorHandler.getMessage(generator, response);

				throw new Exception(errorMessage);
			}
		}

		return callsetList;
	}

	public List<VariantSet> getVariantSets()
		throws Exception
	{
		List<VariantSet> vList = new ArrayList<>();
		Pager pager = new Pager();

		while (pager.isPaging())
		{
			Response<BrapiListResource<VariantSet>> response = service.getVariantSets(null, null, studyID, null, null, pager.getPageSize(), pager.getPage())
				.execute();

			if (response.isSuccessful())
			{
				BrapiListResource<VariantSet> variantSet = response.body();

				vList.addAll(variantSet.data());
				pager.paginate(variantSet.getMetadata());
			}
			else
			{
				String errorMessage = ErrorHandler.getMessage(generator, response);

				throw new Exception(errorMessage);
			}

		}

		return vList;
	}

	public VariantSet getVariantSet()
		throws Exception
	{
		VariantSet variantSet = null;

		Response<BrapiBaseResource<VariantSet>> response = service.getVariantSetById(variantSetID)
			.execute();

		if (response.isSuccessful())
		{
			BrapiBaseResource<VariantSet> r = response.body();

			variantSet = r.getResult();
		}
		else
		{
			String errorMessage = ErrorHandler.getMessage(generator, response);

			throw new Exception(errorMessage);
		}

		return variantSet;
	}

	// Returns a list of CallSetCallsDetail objects, where each object defines
	// an intersection of line/marker (and hence allele) information
	//
	// BRAPI: /variantsets/{variantSetDbId}/calls
	public HashMap<String,String> getCallSetCallsDetails(File cacheFile)
		throws Exception
	{
		// Reset counters
		markers = new HashMap<String,String>();
		lines = new HashMap<String,String>();
		alleles = 0;

		BufferedWriter out = new BufferedWriter(new FileWriter(cacheFile));

//		List<CallSetCallsDetail> list = new ArrayList<>();
		TokenPager pager = new TokenPager();

		int page = 0;
		while (pager.isPaging() && isOk)
		{
			// Be VERY careful here with the page token pagination method
			// We only have the *current* page's infomation, so the next page we
			// need to ask for is pager.getNextPageToken() even though the
			// method parameter itself is just called pageToken
			Response<BrapiMasterDetailResourcePageToken<CallSetCalls>> response = service.getVariantSetCalls(variantSetID, pager.getPageSize(), pager.getNextPageToken())
				.execute();

			if (response.isSuccessful())
			{
				BrapiMasterDetailResourcePageToken<CallSetCalls> r = response.body();

//				list.addAll(r.getResult().getData());
				pager.paginate(r.getMetadata());

				// Cache each line/marker/allele intersection to disk
				for (CallSetCallsDetail detail: r.getResult().getData())
				{
					lines.put(detail.getCallSetName(), "");
					markers.put(detail.getVariantName(), "");
					alleles++;

					out.write(detail.getCallSetName() + "\t"
						+ detail.getVariantName() + "\t"
						+ detail.getGenotype().getValues().get(0));
					out.newLine();
				}

/*				System.out.println("data paged:");
				System.out.println("  getNextPageToken: " + pager.getNextPageToken());
				System.out.println("  getPageToken:     " + pager.getPageToken());
				System.out.println("  getPrevPageToken: " + pager.getPrevPageToken());
				System.out.println("  getPageSize:      " + pager.getPageSize());
				System.out.println("  getTotalPages:    " + pager.getTotalPages());

				System.out.println("list size is " + list.size());
*/
				ioHeteroSeparator = r.getResult().getSepUnphased();
				ioMissingData = r.getResult().getUnknownString();

				System.out.println("Processed page " + (++page) + " out of " + pager.getTotalPages());
			}
			else
			{
				String errorMessage = ErrorHandler.getMessage(generator, response);

				out.close();
				throw new Exception(errorMessage);
			}
		}

		out.close();

		return markers;
	}

	public XmlBrapiProvider getBrapiProviders()
		throws Exception
	{
		URL url = new URL("https://ics.hutton.ac.uk/resources/brapi/flapjack-brapi-202002-v2.0.zip");

		File dir = new File(FlapjackUtils.getCacheDir(), "brapi");
		dir.mkdirs();

		// Download the zip file and extract its contents into a temp folder
		ZipInputStream zis = new ZipInputStream(new BufferedInputStream(url.openStream()));
		ZipEntry ze = zis.getNextEntry();

		while (ze != null)
		{
			BufferedOutputStream out = new BufferedOutputStream(
				new FileOutputStream(new File(dir, ze.getName())));
			BufferedInputStream in = new BufferedInputStream(zis);

			byte[] b = new byte[4096];
			for (int n; (n = in.read(b)) != -1; )
				out.write(b, 0, n);

			out.close();
			ze = zis.getNextEntry();
		}
		zis.closeEntry();
		zis.close();


		// Now read the contents of the XML file
		JAXBContext jaxbContext = JAXBContext.newInstance(XmlBrapiProvider.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		File xml = new File(dir, "brapi.xml");

		return (XmlBrapiProvider) jaxbUnmarshaller.unmarshal(xml);
	}

	public boolean requiresAuthentication()
		throws Exception
	{
		Pager pager = new Pager();

		// Check if the studies call requires authentication
		int responseCode = service.getStudies(null, "genotype", null, null,
			null, null, null, null, null, null, null, null, null, null,
			null,  pager.getPageSize(), pager.getPage())
			.execute().code();

		// 401 and 403 represent the two possible unauthorized / unauthenticated response codes
		return responseCode == 401 || responseCode == 403;
	}

	// Use the okhttp client we configured our retrofit service with. This means
	// the client is configured with any authentication tokens and any custom
	// certificates that may be required to interact with the current BrAPI
	// resource
	InputStream getInputStream(URI uri)
		throws Exception
	{
		// TODO: Don't return the bytestream directly, check status codes wherever we hit URIs
		return generator.getResponse(uri).body().byteStream();
	}

	okhttp3.Response getResponse(URI uri)
		throws Exception
	{
		return generator.getResponse(uri);
	}

	public String currentAsyncStatusMessage()
	{
		return status.toString();
	}

	public void removeAuthHeader()
	{
		service = generator.removeAuthHeader();
	}


	public String getUsername()
		{ return username; }

	public void setUsername(String username)
		{	 this.username = username; }

	public String getPassword()
		{ return password; }

	public void setPassword(String password)
		{ this.password = password; }

	public XmlResource getResource()
		{ return resource; }

	public void setResource(XmlResource resource)
		{ this.resource = resource; }

	public String getMapID()
		{ return mapID; }

	public void setMapID(String mapIndex)
		{ this.mapID = mapIndex; }

	public String getStudyID()
		{ return studyID; }

	public void setStudyID(String studyID)
		{ this.studyID = studyID; }

	public String getVariantSetID()
		{ return variantSetID; }

	public void setVariantSetID(String variantSetID)
		{ this.variantSetID = variantSetID; }

	public String getVariantSetName()
		{ return variantSetName; }

	public void setVariantSetName(String variantSetName)
		{ this.variantSetName = variantSetName; }

	public long getTotalMarkers()
	{
		return totalMarkers;
	}

	public void setTotalMarkers(long totalMarkers)
	{
		this.totalMarkers = totalMarkers;
	}

	public long getTotalLines()
	{
		return totalLines;
	}

	public void setTotalLines(long totalLines)
	{
		this.totalLines = totalLines;
	}

	public void cancel()
	{
		isOk = false;
		generator.cancelAll();
	}

	public String getIoMissingData()
		{ return ioMissingData; }

	public String getIoHeteroSeparator()
		{ return ioHeteroSeparator; }

	public int jsonMarkerCount()
		{ return markers.size(); }

	public int jsonLineCount()
		{ return lines.size(); }

	public long jsonAlleleCount()
		{ return alleles; }
}