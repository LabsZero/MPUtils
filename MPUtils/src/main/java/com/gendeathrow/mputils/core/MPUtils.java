package com.gendeathrow.mputils.core;

import java.io.IOException;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import org.apache.logging.log4j.Logger;

import com.gendeathrow.mputils.core.proxies.MPCommonProxy;
import com.gendeathrow.mputils.utils.NewMPInfo;
import com.gendeathrow.mputils.utils.Tools;
import com.google.common.eventbus.Subscribe;

// 1.10.2 Minecraft

@Mod(modid = MPUtils.MODID, name=MPUtils.NAME, version = MPUtils.VERSION, guiFactory = "com.gendeathrow.mputils.configs.ConfigGuiFactory")
public class MPUtils 
{
    public static final String MODID = "mputils";
    public static final String VERSION = "1.2.9";
    public static final String NAME = "MPUtils";
    public static final String PROXY = "com.gendeathrow.mputils.core.proxies";
    

    public static final String MCVERSION = "1.0.0";
    public static final String VERSION_MAX = "1.0.0";
	public static final String version_group = "required-after:" + MODID + "@[" + VERSION + "," + VERSION_MAX + ");";
	
	
    @Instance(MODID)
	public static MPUtils instance;
    
	@SidedProxy(clientSide = PROXY + ".MPClientProxy", serverSide = PROXY + ".MPCommonProxy")
	public static MPCommonProxy proxy;
    
	private SimpleNetworkWrapper network;
	
    public static Logger logger;
    
    @Subscribe
    public void modConstruction(FMLConstructionEvent evt)
    {
 
		NewMPInfo mp = new NewMPInfo();
		Loader.instance().getModList().add(mp);
    }
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    	
    	logger = event.getModLog();
    	
    	proxy.preInit(event);
    	
try {
	Tools.test();
} catch (IOException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
		this.network = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
		//this.network.registerMessage(PacketReaderInfo.Handler.class, PacketReaderInfo.class, 0, Side.SERVER);
    }
	
   
    @EventHandler
    public void init(FMLInitializationEvent event) throws IOException
    {
    	//Tools.sendpost();
    	NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
    	
    	proxy.init(event);
     }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
    	proxy.postInit(event);
    }
    
	@EventHandler
	public void serverStart(FMLServerStartingEvent event)
	{

	}
    
}
