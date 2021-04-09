package com.mobilous.ext.plugin.impl;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.itextpdf.text.DocumentException;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.mobilous.ext.plugin.PluginService;
import com.mobilous.ext.plugin.config.PluginServiceConfig;
import com.mobilous.ext.plugin.constant.Constant;
import com.mobilous.ext.plugin.constant.DatasetKey;
import com.mobilous.ext.plugin.schema.DataType;
import com.mobilous.ext.plugin.util.HeterogeneousMap;


import net.xeoh.plugins.base.annotations.Capabilities;
import net.xeoh.plugins.base.annotations.PluginImplementation;


@PluginImplementation
public class PluginServiceImpl implements PluginService {

	private static final String serviceName = PluginServiceConfig.MY_SERVICE_NAME.getValue();

	@Capabilities
	public String[] capabilities() {
		return new String[] {serviceName};
	}



	/*
	 * Returns the number of fields on each request.
	 */
	public HeterogeneousMap numrecord(HeterogeneousMap dataset) throws Exception {

		HeterogeneousMap map = new HeterogeneousMap();
		JSONParser parser = new JSONParser();
		JSONArray arr= new JSONArray();
		
		try {
			map=read(dataset);
		} catch (java.text.ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		map.put(DatasetKey.AUTH_TYPE.getKey(), Constant.CUSTOM.getValue()); // or
		map.put(DatasetKey.SERVICENAME.getKey(), serviceName);

		//System.out.println(map.get(DatasetKey.AUTH_STATUS.getKey()));
		if(map.get(DatasetKey.AUTH_STATUS.getKey()).equals("invalid"))
		{
			map.put(DatasetKey.AUTH_STATUS.getKey(), Constant.RET_STATUS_INVALID.getValue());
			map.put(DatasetKey.RETURN_STATUS.getKey(), Constant.RET_STATUS_INVALID.getValue());
			map.put("numrec", "0");
			return map;
		}else
		{
			//map.put(DatasetKey.RETURN_STATUS.getKey(), Constant.RET_STATUS_VALID.getValue());
			try {
				arr=(JSONArray)parser.parse(map.get(DatasetKey.DATA.getKey()));
				
				map.put("numrec", String.valueOf(arr.size()));
				return map;
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				map.put(DatasetKey.AUTH_STATUS.getKey(), Constant.RET_STATUS_INVALID.getValue());
				map.put(DatasetKey.RETURN_STATUS.getKey(), Constant.RET_STATUS_INVALID.getValue());
				e.printStackTrace();
				return map;
			}	
		}
	
	}


	/**
	 * Please do not modify this method.
	 */
	public HeterogeneousMap getServiceName() {
		
		HeterogeneousMap map = new HeterogeneousMap();
		map.put(DatasetKey.SERVICENAME.getKey(), serviceName);
		
		map.put(DatasetKey.AUTHORIZE_URL.getKey(),"http://stagingconsole.mobilous.com/en/database-manager/"+serviceName);

		map.put("use_api", true, Boolean.class);


		return map;
	}





	public HeterogeneousMap authenticate(HeterogeneousMap dataset) {

		HeterogeneousMap retVal = new HeterogeneousMap();
		// joven code
		retVal.put(DatasetKey.AUTH_TYPE.getKey(), Constant.CUSTOM.getValue()); // or
		// authtype
		retVal.put(DatasetKey.SERVICENAME.getKey(), serviceName);
		retVal.put(DatasetKey.AUTH_STATUS.getKey(), Constant.AUTH_STATUS_VALID.getValue());
		System.out.println("General Login Plugin");
		return retVal;
	}


	@SuppressWarnings("rawtypes")
	public HeterogeneousMap getSchema(HeterogeneousMap dataset) {
		HeterogeneousMap schema = new HeterogeneousMap();
		String tablename = dataset.get(DatasetKey.TABLE.getKey());

		if (tablename == null) {
		    List<String> tables = new ArrayList<String>();

		    tables.add("generate_otp");
		    tables.add("generate_token");
		    tables.add("get_dtor_salesdata");
		    tables.add("get_tally_salesdata");
		    tables.add("get_sms");
		    tables.add("update_dealer");
		    tables.add("send_invoice_mail");
		    tables.add("verify_otp");
		    tables.add("verify_otp_mw");
		    tables.add("send_category_mail");
		    tables.add("user_data");
		    tables.add("deactivate_dealer");
		    tables.add("insert_dealer");
		    tables.add("update_dealer");
		    tables.add("sku_description");
		    
		    schema.put(DatasetKey.SCHEMA.getKey(), tables, List.class);
		} 
		else if (tablename.equals("generate_otp")) {
		    List textFields = new ArrayList();
		    
		    textFields.add("otp");
		    schema.put(DataType.TEXT.getValue(), textFields, List.class);
		}
		else if (tablename.equals("generate_token")) {
		    List textFields = new ArrayList();
		    
		    textFields.add("token");
		    schema.put(DataType.TEXT.getValue(), textFields, List.class);
		}
	/*	else if (tablename.equals("get_dtor_salesdata")) {
		    List textFields = new ArrayList();
		    
		    textFields.add("FromDate");
		    textFields.add("ToDate");
		    textFields.add("SOTP");
		    textFields.add("invoiceDate");
		    textFields.add("matnr");
		    textFields.add("matdesc");
		    textFields.add("SoldToCode");
		    textFields.add("cusName");
		    textFields.add("dealerID");
		    textFields.add("invQty");
		    textFields.add("invAmt");
		    textFields.add("rate");
		    textFields.add("batchID");
		    textFields.add("timestamp");
		    textFields.add("dealerCode");
		    textFields.add("dealerName");
		    textFields.add("distCode");
		    textFields.add("addressOne");
		    textFields.add("contactPerson");
		    textFields.add("district");
		    textFields.add("state");
		    textFields.add("town");
		    textFields.add("pinCode");
		    textFields.add("mobile");
		    textFields.add("retailCat");
		    textFields.add("weighbridge");
		    textFields.add("intransitDays");
		    textFields.add("creditLimit");
		    textFields.add("route");
		    textFields.add("Data_send_Days");
		    textFields.add("sndleg_Coverage");
		    textFields.add("status");
		    textFields.add("TOC_CRM_FLAG");
		    textFields.add("RETAILER_ZONE");
		    textFields.add("EMAIL");
		    textFields.add("BRAND");
		    textFields.add("RETAILER_CONVNAME");
		    textFields.add("COUNTER_SIZE");
		    textFields.add("PROD_APP");
		    textFields.add("CUSTPOT");
		    textFields.add("ASO");
		    textFields.add("GSTIN");
		    textFields.add("GST_State_Code");
		    textFields.add("GST_Regd_Typ");
		    textFields.add("Bhim");
		    textFields.add("SMName");
		    
		    schema.put(DataType.TEXT.getValue(), textFields, List.class);
		}
		   */
		else if (tablename.equals("get_tally_salesdata")) {
		    List textFields = new ArrayList();
		    
		    textFields.add("FromDate");
		    textFields.add("ToDate");
		    textFields.add("invoiceDate");
		    textFields.add("distName");
		    textFields.add("dealerName");
		    textFields.add("dealerCode");
		    textFields.add("state");
		    textFields.add("district");
		    textFields.add("taluka");
		    textFields.add("PINCODE");
		    textFields.add("customerName");
		    textFields.add("quantitySold");
		    textFields.add("source");
		    textFields.add("SOTP");
		    textFields.add("timestmp");
		    textFields.add("custMobile");
		    textFields.add("tid");
		    
		    schema.put(DataType.TEXT.getValue(), textFields, List.class);
		}
//		else if (tablename.equals("insert_dealer")) {
//		    List textFields = new ArrayList();
//		    
//		    textFields.add("status");
//		    schema.put(DataType.TEXT.getValue(), textFields, List.class);
//		}
		else if (tablename.equals("update_dealer")) {
		    List textFields = new ArrayList();
		    
		    textFields.add("status");
		    schema.put(DataType.TEXT.getValue(), textFields, List.class);
		}
		else if (tablename.equals("get_sms")) {
		    List textFields = new ArrayList();
		    
		    textFields.add("result");
		    schema.put(DataType.TEXT.getValue(), textFields, List.class);
		}
		else if (tablename.equals("send_invoice_mail")) {
		    List textFields = new ArrayList();
		    
		    textFields.add("result");
		    schema.put(DataType.TEXT.getValue(), textFields, List.class);
		}
		else if (tablename.equals("verify_otp")) {
		    List textFields = new ArrayList();
		    
		    textFields.add("result");
		    schema.put(DataType.TEXT.getValue(), textFields, List.class);
		}
		else if (tablename.equals("verify_otp_mw")) {
		    List textFields = new ArrayList();
		    
		    textFields.add("result");
		    schema.put(DataType.TEXT.getValue(), textFields, List.class);
		}
		else if (tablename.equals("send_category_mail")) {
		    List textFields = new ArrayList();
		    textFields.add("result");
		    schema.put(DataType.TEXT.getValue(), textFields, List.class);
		}
		else if (tablename.equals("user_data")) {
		    List textFields = new ArrayList();

		    textFields.add("custcode");
		    textFields.add("name");
		    textFields.add("address");
		    textFields.add("pincode");
		    textFields.add("state");
		    textFields.add("zone");
		    textFields.add("tel");
		    textFields.add("pannno");
		    textFields.add("GST");
		    schema.put(DataType.TEXT.getValue(), textFields, List.class);
		}
		
		else if (tablename.equals("deactivate_dealer")) {
		    List textFields = new ArrayList();

		    textFields.add("success");
		    textFields.add("errormsg");
		    schema.put(DataType.TEXT.getValue(), textFields, List.class);
		}
		
		else if (tablename.equals("insert_dealer")) {
		    List textFields = new ArrayList();

		    textFields.add("success");
		    textFields.add("errormsg");
		    schema.put(DataType.TEXT.getValue(), textFields, List.class);
		}
		
		else if (tablename.equals("update_dealer")) {
		    List textFields = new ArrayList();

		    textFields.add("success");
		    textFields.add("errormsg");
		    schema.put(DataType.TEXT.getValue(), textFields, List.class);
		}
		
		
		else if (tablename.equals("sku_description")) {
		    List textFields = new ArrayList();

		    textFields.add("skucode");
		    textFields.add("skudescription");
		    schema.put(DataType.TEXT.getValue(), textFields, List.class);
		}
		return schema;
	}



	public HeterogeneousMap create(HeterogeneousMap dataset) {
		return null;
	}


	public static void uploadImage(String imageName) {


	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public HeterogeneousMap read(HeterogeneousMap dataset) throws Exception   {
		HeterogeneousMap map = new HeterogeneousMap();
		String tablename = dataset.get(DatasetKey.TABLE.getKey());
		
		String where = dataset.get(DatasetKey.WHERE.getKey());
		System.out.println("Tablename============"+tablename);
		System.out.println("Where============"+where);
				
		JSONArray clean = new JSONArray();
		JSONObject obj = new JSONObject();
		
		
		
		if (tablename.equals("generate_otp")) {

			String response = GenerateOtp.generateotp(where);
			obj.put("otp", response);
			System.out.println(obj);
			clean.add(obj);		

			
		    
		    map.put(DatasetKey.AUTH_STATUS.getKey(), Constant.RET_STATUS_VALID.getValue());
			map.put(DatasetKey.RETURN_STATUS.getKey(), Constant.RET_STATUS_VALID.getValue());
			map.put(DatasetKey.DATA.getKey(), clean, JSONArray.class);
			return map;

		}
		if (tablename.equals("generate_token")) {

			String response = GenerateBearerToken.generatebearer(where);
			obj.put("token", response);
			clean.add(obj);		

			
		    
		    map.put(DatasetKey.AUTH_STATUS.getKey(), Constant.RET_STATUS_VALID.getValue());
			map.put(DatasetKey.RETURN_STATUS.getKey(), Constant.RET_STATUS_VALID.getValue());
			map.put(DatasetKey.DATA.getKey(), clean, JSONArray.class);
			return map;

		}
//		if (tablename.equals("get_dtor_salesdata")) {
//
//			clean = GetD2RSalesData.getdtorsalesdata(where);
//			System.out.println(clean);
//			
//		    map.put(DatasetKey.AUTH_STATUS.getKey(), Constant.RET_STATUS_VALID.getValue());
//			map.put(DatasetKey.RETURN_STATUS.getKey(), Constant.RET_STATUS_VALID.getValue());
//			map.put(DatasetKey.DATA.getKey(), clean, JSONArray.class);
//			return map;
//
//		}
//		if (tablename.equals("get_tally_salesdata")) {
//
//			clean = GetTallySalesData.gettallysalesdata(where);
//			System.out.println(clean);
//
//			
//			map.put(DatasetKey.AUTH_STATUS.getKey(), Constant.RET_STATUS_VALID.getValue());
//			map.put(DatasetKey.RETURN_STATUS.getKey(), Constant.RET_STATUS_VALID.getValue());
//			map.put(DatasetKey.DATA.getKey(), clean, JSONArray.class);
//			return map;
//
//		}
//		if (tablename.equals("insert_dealer")) {
//
//			boolean response = InsertDealerDetails.insertdealerdetails(where);
//			obj.put("status", response);
//			clean.add(obj);	
//			System.out.println(clean);
//
//			
//			map.put(DatasetKey.AUTH_STATUS.getKey(), Constant.RET_STATUS_VALID.getValue());
//			map.put(DatasetKey.RETURN_STATUS.getKey(), Constant.RET_STATUS_VALID.getValue());
//			map.put(DatasetKey.DATA.getKey(), clean, JSONArray.class);
//			return map;
//
//		}
//		if (tablename.equals("update_dealer")) {
//
//			boolean response = ModifyDealerDetails.modifydealerdetails(where);
//			obj.put("status", response);
//			clean.add(obj);	
//			System.out.println(clean);
//
//			
//			map.put(DatasetKey.AUTH_STATUS.getKey(), Constant.RET_STATUS_VALID.getValue());
//			map.put(DatasetKey.RETURN_STATUS.getKey(), Constant.RET_STATUS_VALID.getValue());
//			map.put(DatasetKey.DATA.getKey(), clean, JSONArray.class);
//			return map;
//
//		}
		if (tablename.equals("get_sms")) {

			String response = GenerateSms.generatesms(where);
			obj.put("result", response);
			clean.add(obj);	
			System.out.println(clean);

			
			map.put(DatasetKey.AUTH_STATUS.getKey(), Constant.RET_STATUS_VALID.getValue());
			map.put(DatasetKey.RETURN_STATUS.getKey(), Constant.RET_STATUS_VALID.getValue());
			map.put(DatasetKey.DATA.getKey(), clean, JSONArray.class);
			return map;

		}
//		if (tablename.equals("send_invoice_mail")) {
//
//			String response = GeneratePdf.generatepdf(where);
//			obj.put("result", response);
//			clean.add(obj);	
//			System.out.println(clean);
//
//			
//			map.put(DatasetKey.AUTH_STATUS.getKey(), Constant.RET_STATUS_VALID.getValue());
//			map.put(DatasetKey.RETURN_STATUS.getKey(), Constant.RET_STATUS_VALID.getValue());
//			map.put(DatasetKey.DATA.getKey(), clean, JSONArray.class);
//			return map;
//
//		}
		if (tablename.equals("verify_otp")) {

			String response = VerifyOtp.verifyotp(where);
			obj.put("result", response);
			clean.add(obj);	
			System.out.println(clean);

			
			map.put(DatasetKey.AUTH_STATUS.getKey(), Constant.RET_STATUS_VALID.getValue());
			map.put(DatasetKey.RETURN_STATUS.getKey(), Constant.RET_STATUS_VALID.getValue());
			map.put(DatasetKey.DATA.getKey(), clean, JSONArray.class);
			return map;

		}
		
		if (tablename.equals("verify_otp_mw")) {

			String response = VerifyOtpMw.verifyotpmw(where);
			obj.put("result", response);
			clean.add(obj);	
			System.out.println(clean);

			
			map.put(DatasetKey.AUTH_STATUS.getKey(), Constant.RET_STATUS_VALID.getValue());
			map.put(DatasetKey.RETURN_STATUS.getKey(), Constant.RET_STATUS_VALID.getValue());
			map.put(DatasetKey.DATA.getKey(), clean, JSONArray.class);
			return map;

		}
//		if (tablename.equals("send_category_mail")) {
//
//			String response = SendCMTEmail.sendemail(where);
//			obj.put("result", response);
//			clean.add(obj);	
//			System.out.println(clean);
//
//			
//			map.put(DatasetKey.AUTH_STATUS.getKey(), Constant.RET_STATUS_VALID.getValue());
//			map.put(DatasetKey.RETURN_STATUS.getKey(), Constant.RET_STATUS_VALID.getValue());
//			map.put(DatasetKey.DATA.getKey(), clean, JSONArray.class);
//			return map;
//
//		}
		
		if (tablename.equals("user_data")) {
			AutofillViaSAP auto = new AutofillViaSAP();
			try{
				clean = auto.getUserData(where);
			}
			catch(Exception e) {
				e.printStackTrace();
				map.put((DatasetKey.AUTH_STATUS.getKey()), Constant.RET_STATUS_INVALID.getValue());
				map.put((DatasetKey.RETURN_STATUS.getKey()), Constant.RET_STATUS_INVALID.getValue());
				map.put(DatasetKey.DATA.getKey(), clean, JSONArray.class);
			}
		}
		
		if(tablename.equals("deactivate_dealer")) {
			ActivateAndDeactivateDealer auto = new ActivateAndDeactivateDealer();
			try{
				clean = auto.deactivateDealer(where);
			}
			catch(Exception e) {
				e.printStackTrace();
				map.put((DatasetKey.AUTH_STATUS.getKey()), Constant.RET_STATUS_INVALID.getValue());
				map.put((DatasetKey.RETURN_STATUS.getKey()), Constant.RET_STATUS_INVALID.getValue());
			}
		}
		
		if(tablename.equals("insert_dealer")) {
			InsertDealer auto = new InsertDealer();
			try{
				clean = auto.insertDealer(where);
			}
			catch(Exception e) {
				e.printStackTrace();
				map.put((DatasetKey.AUTH_STATUS.getKey()), Constant.RET_STATUS_INVALID.getValue());
				map.put((DatasetKey.RETURN_STATUS.getKey()), Constant.RET_STATUS_INVALID.getValue());
			}
		}
		
		if(tablename.equals("update_dealer")) {
			UpdateDealer auto = new UpdateDealer();
			try{
				clean = auto.updateDealer(where);
			}
			catch(Exception e) {
				e.printStackTrace();
				map.put((DatasetKey.AUTH_STATUS.getKey()), Constant.RET_STATUS_INVALID.getValue());
				map.put((DatasetKey.RETURN_STATUS.getKey()), Constant.RET_STATUS_INVALID.getValue());
			}
		}

		
		
		if(tablename.equals("sku_description")) {
			FetchSKUDescription auto = new FetchSKUDescription();
			try{
				clean = auto.fetchSkuDescription(where);
			}
			catch(Exception e) {
				e.printStackTrace();
				map.put((DatasetKey.AUTH_STATUS.getKey()), Constant.RET_STATUS_INVALID.getValue());
				map.put((DatasetKey.RETURN_STATUS.getKey()), Constant.RET_STATUS_INVALID.getValue());
			}
		}
		
		map.put(DatasetKey.AUTH_STATUS.getKey(), Constant.RET_STATUS_VALID.getValue());
		map.put(DatasetKey.RETURN_STATUS.getKey(), Constant.RET_STATUS_VALID.getValue());
		map.put(DatasetKey.DATA.getKey(), clean, JSONArray.class);
		System.out.println("Clean : "+clean);
		return map;
}

	public HeterogeneousMap update(HeterogeneousMap dataset) {
		return null;
	}


	public HeterogeneousMap delete(HeterogeneousMap dataset) {
		return null;
	}
}
