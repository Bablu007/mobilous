package com.mobilous.ext.plugin;

import com.mobilous.ext.plugin.constant.DatasetKey;
import com.mobilous.ext.plugin.impl.*;
import com.mobilous.ext.plugin.util.HeterogeneousMap;

public class Stub {

	
	public static void main(String[] args) throws Exception  
	{

		PluginServiceImpl plugin = new PluginServiceImpl();
		HeterogeneousMap map = new HeterogeneousMap();
		map.put(DatasetKey.WHERE.getKey(), "9971848804");
		map.put(DatasetKey.TABLE.getKey(), "generate_otp");
		/* map.put(DatasetKey.WHERE.getKey(), "1676410001;0000167641;R");
		map.put(DatasetKey.TABLE.getKey(), "deactivate_dealer");
		map.put(DatasetKey.WHERE.getKey(), "AGRMOB115;0000111111;testdealer;West;Gujrat;testaddrss;test@gmail.com;sayani;7763807826;"
				+ "anand;surat;19ASPPB7405H1ZO;X;RB;19;REGULAR;700156");
		map.put(DatasetKey.TABLE.getKey(), "insert_dealer");
		
		map.put(DatasetKey.WHERE.getKey(), "AGRMOB115;0000111111;testdealer;West;Gujrat;testaddrss;test@gmail.com;sayani;7763807826;"
				+ "anand;surat;19ASPPB7405H1ZO;X;RB;19;REGULAR;700156;X");
		map.put(DatasetKey.TABLE.getKey(), "update_dealer"); */
		plugin.read(map);
	}

};