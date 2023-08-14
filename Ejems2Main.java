
/****************************************************************************************
 *																						*
 *	File name   : Ejems2Main.java														*
 *																						*
 *																						*
 *	COPYRIGHT NOTICE. Copyright � 2001 House of Code, Inc., 589 Fifth Avenue, Suite 	*
 *	1004, New York, NY 10017 All rights reserved. This software and documentation is	*
 *	subject to and made available only pursuant to the terms of the House of Code		*
 *	License Agreement and may be used or copied only in accordance with the terms of	*
 *	that agreement. It is against the law to copy the software except as specifically	*
 *	allowed in the agreement. This document may not, in whole or in part, be copied,	*
 *	photocopied, reproduced, translated, or reduced to any electronic medium or			*
 *	machine-readable form without prior consent, in writing, from House of Code, Inc. 	*
 *																						*
 *																						*
 ****************************************************************************************/


package com.hoc.ejems2.client.frames;

import com.hoc.ejems2.client.util.ClientUtility;
import com.hoc.ejems2.client.util.Splash;
import com.hoc.ejems2.client.util.StatusBar;
import com.hoc.ejems2.companymaster.model.CompanyModel;
import com.hoc.ejems2.ejemsmain.session.IEjemsMainMgrHome;
import com.hoc.ejems2.ejemsmain.session.IEjemsMainMgrRemote;
import com.hoc.ejems2.security.model.NetRightsModel;
import com.hoc.ejems2.security.session.login.IUserLoginMgrHome;
import com.hoc.ejems2.security.session.login.IUserLoginMgrRemote;
import com.hoc.ejems2.util.BoundObject;
import com.hoc.ejems2.util.Debug;
import com.hoc.ejems2.util.EJBUtil;
import com.hoc.ejems2.images.model.ImageModel;	//PNS21JULY2003
import com.hoc.ejems2.images.session.*;			//PNS21JULY2003
import java.util.ArrayList;						//PNS21JULY2003
// TM 4/29/2008 Commented temporary
import com.hoc.ejems2.solitairerequirement.session.ISolRequirementMgrHome;
import com.hoc.ejems2.solitairerequirement.session.ISolRequirementMgrRemote;

//UL:10/7/2008
import com.hoc.ejems2.unmarkholdsolitaires.session.IUnmarkHoldSolitairesMgrHome;
import com.hoc.ejems2.unmarkholdsolitaires.session.IUnmarkHoldSolitairesMgrRemote;


import com.hoc.ejems2.logmaster.model.ModuleLogModel;//SM/7/14/2010
import com.hoc.ejems2.logmaster.session.*;//SM/7/14/2010





//RS/09/12/2005
/*
PLEASE DO NOT DELETE THE BELOW COMMENTED CODE.
import com.hoc.licensing.model.SystemComponent;
import com.hoc.licensing.session.ILicenseMgrHome;
import com.hoc.licensing.session.ILicenseMgrRemote;
import com.hoc.licensing.util.LicenseUtility;*/
//RS/09/12/2005

import org.apache.xpath.XPathAPI;
import org.w3c.dom.Document;
import org.w3c.dom.Element;//HRD

import javax.ejb.CreateException;
import javax.naming.NamingException;
import javax.swing.*;
import javax.swing.event.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Constructor;
import java.rmi.RemoteException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import java.io.IOException;

//TM 1/16/2008 Start
import java.util.Timer;
import java.util.TimerTask;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties; 
import java.io.FileInputStream;
//TM 1/16/2008 End
//SM/7/14/2010
import java.sql.Timestamp;

/**
 *
 *  @GUI design :
 *  @author     : EJEMS2 team
 *  @version    : 1.0.
 *  @Dated      : 02/02/2002.
 *
 *  Modification History:
 *
 *  Date		Modified By     Reason														Tag
 *  ====		===========     ========================================					===
 *	02/02/2002	Prakash,		Clean up, defined mneumonics for
 *				Ashish			Job contracting, tools, inventory
 *								windows and help menu
 *								Added not available popup message
 *								for those menus where it is not
 *								available currently
 *
 *	10/04/2002	Prakash,		Common method addMenuItem() to
 *				Ashish			all menu items to the menu bar
 * 								and all listeners in one single
 *								file. Also Radhika added
 *								functionality where the
 *								BoundObject is stored in
 *								Ejems2Main so that whenver
 *								it is required the
 *								InternalFrameTemplete gets
 *								it from here instead
 *								of from registry on server.
 *
 *	18/07/2002  Ammar           Added Code for Status Bar// Status Panel
 *
 *	20/07/2002  Ammar           Deleted the previously added code for // Status Panel
 *                              the Status Bar and used Status Bar
 *                              class which is in client.util for Status Bar.
 *
 *	27/07/2002  Prakash         For new modules, often security is not						PNS27072002
 *								defined in netrights table. This was
 *								causing login to fail. This is caught.
 *								This is handled in addMenuItem. Menu
 *								is not added if the menu entry is not
 *								found the security hashtable
 *	30/07/2002  Prakash         About window added											PNS30072002
 *
 *	31/07/2002  Prakash         Menu separator added in Inventory menu						PNS31072002
 *
 *	21/10/2002  Ammar           Added Repair MenuItem and separtor in						asl 10-21-2002
 *                              JobContracting menu
 *
 *	02/12/2002  Prakash         Added support to read user's preferences on					PNS02DEC2002
 *								load. User's preferences are found in directory
 *								called 'userpref-inf' under client/util/table folder.
 *								This file is an XML file storing user's preferences.
 *								Currently, it stores information on a table's layout.
 *								User can save column's width and column's sequence.
 *								Since this XML file is going to have more preferences
 *								saved, it is better to read this information once.
 *								A method is added to read XML file and kept in memory
 *								as DOM tree. EModuleXMLReader will be reading this
 *								DOM tree to merge user's preferences with system's
 *								properties. And eventually, ETablePanel will be reading
 *								this to sequence table.
 *
 *	31/12/2002	Nikhil			Added Module Master menuitem in Security menu				NAM31122002
 *
 *	29JAN03		Nikhil			IEjems2MgrHome renamed to ILicenseMgrHome					NAM290103
 *								IEjems2MgrRemote renamed to ILicenseMgrRemote
 *
 *	13/02/2003  Prakash         Two methods added : ReadValue and WriteValue.				PNS13FEB2003
 *								A helper method : SaveXMLFile added.
 *								ReadValue: This will allow any program to read configuration
 *								related values to be read from the user's preference file
 *								stored as XML. <configuration><section><item>...
 *								WriteValue: THis will allow any program to write configuration
 *								related values to the user's preference file.
 *								For example, it is now possible that JIFraReport program, on
 *								form close, can write which report was opened for the given
 *								user. And therefore, when user opens ejems again and goes to
 *								report module, that report will be opened again. For this,
 *								report module will write last access report using "WriteValue".
 *								And then on load, read this value using "ReadValue"
 *
 *	14/02/2003  Prakash         A manager bean for ejems2main has been added.				PNS14FEB2003
 *								It is called : ejemsmain.
 *								All XML files (user preference and module/table descriptor
 *								xml files) are now retrieved and saved via this session
 *								manager class. "saveXMLFile" method removed.
 *
 *	20th Feb 03	Vimal Shah		Commented adding of invoice scans to menu as they are		[vhs-2/20/2003]
 *								not yet developed
 *
 *	28/03/2003	Jaishali Dhake	Added Gold/Platinum Price on form load and
 *								submenu in Tools menu										JAY28/03/2003
 *
 *	08/04/2003	Nikhil			New method added to get company model						NAM08APR03
 *
 *	29/04/2003  Siddhi			Added methods to create userdefined xml tags				SJ29042003
 *
 *	10/06/2003	Nikhil			Security implemented for Barcode print module				NAM10JUN03
 *
 *	21/07/2003	Prakash			addMenuItemJDialog method dropped. Customization			PNS21JULY2003
 *								made to addMenuItem method. Dropped method was used
 *								to open dialog box of bullion price.
 *								Background image on the desktop is being fetched from
 *								the company master.
 *								It is an image which is NOT primary image
 *
 *	22/07/2003	Prakash			During change of company, sometimes image of				PNS22JULY2003
 *								the old company
 *								was not removed properly.
 *
 *	31/07/2003	Nikhil			Menu Items for Rounding Master and Bar Code Print			NAM31JUL03
 *								moved from "Tools" to "Masters"
 *
 *
 *	31/07/2003	Nikhil			New menu added for Solitaire Component module under			NAM31JUL03
 *								"Job Contracting". This module has been developed at Onsite.
 *
 *	06/08/2003	Ammar			The frame was going behind the image on minimising,			asl 06-08-03
 *								which was a defect.this defect is now fixed.
 *
 *  14/11/03	Siddhi			Added a Menu Item of Retur To Vendor						SJ14112003
 *
 *	13/04/2004	Sachin			Added a Menu Item for PriceLog to the Security Menu			saa 4/13/04
 *				Ankolekar
 *
 *	5/25/04		Devendra		Added a Menu Item for Message Center to the					daa 5/25/04
 * 				Advani			Options menu.
 *
 *	07/06/2004	Sachin			Added a Menu Item for Inventory Requirement to the			saa 6/7/04
 *				Ankolekar		Purchase Menu.
 *
 *	8/10/04		Devendra		StatusBar Class constructor was changed to incorporate		daa-8/10/04
 *				Advani			notification panel for message center, so StatusBar
 *								Class declaration is changed.
 *								
 *	8/16/04		Devendra		Bug Fixing:													daa-8/16/04
 *				Advani			Removed the comment pane.remove(jpnlStatus), which 
 *								did not refresh the status bar on change company
 *								option in file menu.
 * 08/16/04	 	Reshma		Added a new menu JIfraParcelCreation.Certain		RS/08/09/04
					Sanzgiri		conditions set for it under addMenuItem method.									
*						
 *	1/17/05		Suken Shah		Added a new menu item for setting  gold surcharge flag for	  ss 1/7/05						
 *								customers
 *
 *	01/21/2005	AnilKumar		Increased the Mian window size								ANI1/21/2005
 *							
 *	2/24/05     Suken Shah		Added a new menu item to open the call center JSP page		ss 2/24/05 						
 *
 *	07/21/2005	AnilKumar		Debug statements removed.									ANI7/21/2005
 *
 *	09/12/2005	Reshma
 *				Sanzgiri		License related code removed								RS/09/12/2005
 *																							RS/10/11/2005
 *
 * 11/24/2005	Suken Shah		Security Was not getting checked for gold surcharge			ss 11/24/2005
 *								and set Bullion Price.
 *
 *	3/21/2006	AnilKumar		Add a new menu item to opne Reference Vendor Master			ANI3/21/2006
 *								module in master menu.
 *
 *	3/24/2006	Tushar Metkar	Added code in closeApplicaton method to insert logout		TM 3/24/2006
 *								information of the user in EJ2_USER_LOG table.  
 *											
 *	4/21/2006	Tushar Metkar	Bug related User login logout during change company			TM 4/21/2006
 *								is resolved.
 *
 * 5/8/2006		Suken Shah		Solitaire Creation Module added								ss 5/8/2006	
 *
 * 10/10/2006	Reshma Sanzgiri	Label added to display Gold Price							RS/10/10/2006
 *
 * 10/16/2006	Reshma Sanzgiri	GoldPrice field should display the bullion price.			RS/10/16/2006.
 *								Problem was faced with price label when the company			RS/10/17/2006
 *								was changed.												RS/10/19/2006
 *
 * 1/5/2007		Tushar Metkar	Added Sample scan Menu Item in Inventory scan menu.			TM 1/5/2007	
 *
 * 2/21/2007	Suken Shah		Added a module to create orders from katrina				ss 2/21/2007
 *
 * 3/23/2007	Parimal         Added a module for closeout report,                         psk 3/23/2007
 *                              changed report menu by adding submenus				
 *
 * 03/31/2007	Reshma
 *				Sanzgiri		New Module Order Fulfillment added                          RS/03/31/2007
 *                              
 * 5/7/2007		Tushar Metkar	commented temporary.										TM 5/7/2007	
 *                              
 * 05/22/2007	Parimal   	    temporary comments at TM 5/7/2007 uncommented.				PSK 5/22/07					
 *              Kulkarni       
 *                              
 * 06/18/2007	Parimal   	    Added menu for Sales Person Memo Scan.						PSK 6/18/07			
 *              Kulkarni       
 *
 * 7/11/2007	Tushar Metkar	Added Finding scan Menu Item in Inventory scan menu.		TM 7/11/2007
 *
 * 11/23/2007	Parimal			Added image buffer to store images displayed in the			PSK 11/23/07
 *				Kulkarni		transaction lookup.(New buffer is created as earlier
 *								buffer had images which might not be of type 'Normal')
 *
 * 1/16/2008	Tushar Metkar	Added scheduler for Solitaire requirement matching			TM 1/16/2008
 *								functionality
 *
 * 3/5/2008		Tushar Metkar	Added rights for Solitaire requirement matching.			TM 3/5/2008
 *
 * 3/20/2008	Tushar Metkar	Solitaire reqirement matching dialog box is changed to		TM 3/20/2008
 *								Jinternal Frame.	
 *
 * 4/3/2008		Urvashi Lad		Added Item Association memu item in Tools menu.				UL:4/3/2008
 *
 * 4/23/2008	Tushar Metkar	Added Stock List in menu									TM 4/23/2008	
 *
 * 5/7/2008		Urvashi Lad		Added condition while adding item in menu for payment		UL:5/7/2008
 *								details. If it has view then only we have to show it.
 *
 * 5/13/2008	Urvashi Lad		Uncomment the code which is use to remove item association  UL:5/13/2008
 *								from memu.
 *								Added condition while adding item in menu for payment
 *								details. If it has view then only we have to show it.
 *
 * 6/4/2008		Tushar Metkar	Added new module for rapaport price							TM 6/4/2008		
 *
 * 06/06/2008	Parimal			Added new module for generating SI in RJO's EDI format		PSK 6/6/2008		 
 *				Kulkarni
 *
 * 6/6/2008		Tushar Metkar	Rapaport module is commented Temporary.						TM 6/6/2008		
 *
 * 6/18/2008	Tushar Metkar	Change the menu of module Rapaport Price					TM 6/18/2008	
 *
 * 7/3/2008		Tushar Metkar	Comments removed for Solitaire Requisition module.			TM 7/3/2008	
 *
 * 07/15/2008	Reshma 
 *				Sanzgiri		FLAG_SP_COMM_MSG variable addded, value to be fetched 
 *								when startup form opens up.									RS/07/15/2008							
 *
 *  7/23/2008	Shakil Mulani	Added  variable for QUotation module.						SM/7/23/2008
 *
 *  9/15/2008	Urvashi Lad		Added variable for overdue transaction module.				UL:9/15/2008
 *
 *	10/7/2008	Urvashi Lad		Added variable for unmark hold solitaires. Also added		UL:10/7/2008
 *								one method which is use to open the unmark hold solitaire
 *								module when user logged in or change the company.
 *
 *	10/8/2008	Urvashi Lad		Check rights for unmark hold solitaires module				UL:10/8/2008
 *								
 *	05/08/2009 Sheelu Chauhan  	Import Certificate Data Module added						SC 05/08/2009	
 *
 *	5/22/2009	Shakil Mulani	Added Sarin Sol Creation Module								SM5/22/2009
 *
 *
 *  08/17/2009  Sheelu Chauhan   jbt Rating Update Module added                            SC::08/17/2009
 *  9/16/2009	Shakil Mulani   Added Customer Overdue Tracking Module						SM/9/16/2009
 *
 * 09/19/2009  Sheelu Chauhan     Added Category Restriction module                         SC::09/19/2009
 *
 * 11/16/2009	Shakil Mulani	Added new module to import Belk PO							SM/11/16/2009
 *
 * 7/14/2010	Shakil Mulani	Added new module for module log								SM/7/14/2010	
 *
 *	7/19/2010	Shakil Mulani	Module Log related changes done.							Sm/7/19/2010
 *
 * 10/15/2010	Shakil Mulani	Added new module for Out of stock Item						SM/10/15/2010
 *
 * 10/18/2010	Shakil Mulani	Out of stock Item module moved under Sales menu				SM/10/18/2010

 * 12/9/2010    Sheelu Chauhan  Added variable for overstock.								SC::12/9/2010
 */	
 								  

public class Ejems2Main extends JFrame {
	
	/* Start ANI1/21/2005 */
    final int WIDTH = 1055;
    final int HEIGHT = 780;//JDK8
	//END ANI1/21/2005
	
	/** ANI7/21/2005 */
	private static final String STR_CLASS_NAME = "Ejems2Main";

    public Vector internalFramesVect = new Vector();
	public Vector moduleLogVect = new Vector();
    Hashtable hashWindows = new Hashtable();
    JDesktopPane jdtpMain;

    public static Document docUser = null; //PNS02DEC2002

    Container pane;
    Component[] c = null;

    Ejems2Main ej2Main;
    Hashtable htMenuRights;//rc 15

    Hashtable hstInternalFrames = new Hashtable();
    Class objClass = null;
    JCheckBoxMenuItem jmi;
	
	// Menubar
    JMenuBar jmnbMain;

	// File Menu and its sub-menu items
    JMenu jmnuFile;
	JMenuItem jmniChangeCo;
    JMenuItem jmniLogOut;
    JSeparator jspExit;
    JMenuItem jmniExit;
	JMenuItem jmniSalesAnalysis; //ss 2/24/05

	JMenuItem jmniCreateSalesInvoice;//rs/03/31/2007

	// Sales Menu and its sub-menu items
    JMenu jmnuSales;

	// Purchase Menu and its sub-menu items
    JMenu jmnuPurchase;

	// Job Contracting
    JMenu jmnuJobContracting;

	//Inventory Menu and its sub-menu items
    JMenu jmnuInventory;

	// Masters Menu and its sub-menu items
    JMenu jmnuMasters;

    JSeparator jsCustomer;
    JSeparator jsCode;

    JMenu jmnuPricing;

    JSeparator jsCompany;
	
	JMenu jmniStyleCost;

	// Tools Menu and its sub-menu items
    JMenu jmnuTools;
	// Added by JAY28/03/2003
	JMenuItem jmniBullionPrice;
	// End by JAY28/03/2003

	// Security Menu and its sub-menu items
    JMenu jmnuSecurity;

	// Scans Menu and its sub-menu items
    JMenu jmnuScans;
    JMenuItem jmniCustomerScan;
    JMenuItem jmniVendorScan;
    JMenu jmnuSalesScans;
    JMenuItem jmniSalesOrderScan;
    JMenuItem jmniSalesMemoScan;
    JMenuItem jmniSalesInvScan;
    JMenu jmnuPurScans;
    JMenuItem jmniPurOrderScan;
    JMenuItem jmniPurMemoScan;
    JMenuItem jmniVendInvScan;
    JMenu jmnuInventoryScans;
    JMenuItem jmniStyleMasterScan;
    JMenuItem jmniDiamondMasterScan;
    JMenuItem jmniSolitaireScan;
    JMenuItem jmniColStMasterScan;
    JMenuItem jmniMetalMasterScan;
    JMenuItem jmniFindMasterScan;
    JMenuItem jmniCastMasterScan;
    JMenuItem jmniPearlMasterScan;

	//Taskpad - asl 29Aug03
	JMenuItem jmniTaskpad;

	// Report Menu and its sub-menu items
    JMenu jmnuReports;
    JMenuItem jmniReports;
	JMenu jmnuFinanceReports; //psk 3/23/2007
	JMenu jmnuOtherReports; //psk 3/23/2007

	// Window Menu and its sub-menu items
    JMenu jmnuWindow;
    JMenuItem jmniWindowCascade;
    JMenuItem jmniWindowCloseAll;

	// Help Menu and its sub-menu items
    JMenu jmnuHelp;
    JMenuItem jmniIndex;
    JMenuItem jmniWeb;
    JSeparator jsHelp;//rc jan 11
    JMenuItem jmniAbout;

    int iAddRights;
    int iViewRights;
    int iModifyRights;
    int iDeleteRights;
    int iSURights;

    Vector vecKeys = new Vector();

    private boolean bFlagPricing = false;
    private boolean bFlagStyleCostList = false;
    private boolean bFlagSalesScan = false;
    private boolean bFlagPurchaseScan = false;
    private boolean bFlagInvScan = false;

    public static BoundObject objBO;
    StatusBar jpnlStatus;

    /**
     This variable is referred by all modules to decide whether the dirty flag warning
     message should be shown to the user or not. We are providing a feature wherein the
     user can turn OFF the dirty flag warning by setting this variable value to 'false'.
     And in all the modules the code where the question is asked whether to save the
     record or not a check for this boolean is made. If this boolean is set then only
     dirty flag warnings are shown.
     */
    public boolean bShowDirtyDataMessage;

    /** THIS STATIC HASHMAP STORES THE ERROR CODES THAT COME FROM BOUNDOBJECT WHEN THE
     * USER LOGS IN. THE <CODE>UserLoginMgr</CODE> SESSION BEAN READS IT AFTER READING
     * THE RIGHTS FOR THE USER THAT IS LOGGED IN */
    public static HashMap ERROR_CODES;

	/* [vhs-2/25/2003] Variable to hold default freight charges for the logged in company */
	public static double DEFAULT_FREIGHT = 0; // asl 10oct03

	/* [vhs-2/25/2003] Variable to hold default inventory type for the logged in company */
	public static String DEFAULT_INV = ""; // asl 10oct03

	public static String FLAG_SP_COMM_MSG = ""; //RS/07/15/2008

    // Reqd by Metro for Floating Images
    public static HashMap objImageMap = new HashMap();
    public static String strLastImagePath = "";
    public static int intMaxImagesInCache = 50;

	//PSK 11/23/07 - Image buffer for images which are 'primary' and have web image type as 'Normal' 
	//(Used in transaction lookup as 'objImageMap' might have images which are not of type 'Normal')
	public static HashMap objImageBuffer = new HashMap(); 
    public static int intMaxImagesInBuffer = 250;
	public static ArrayList arrlImageBufferQueue = new ArrayList(); //This arraylist is used as a queue to remove first image added to the 'objImageBuffer'
	//When new item is added in 'objImageBuffer' , the key is also added in 'arrlImageBufferQueue'.
	//When size of 'objImageBuffer' exceeds 'intMaxImagesInBuffer', 0th item is taken from 'arrlImageBufferQueue' and removed from both 'objImageBuffer' and 'arrlImageBufferQueue' and than add new image to the buffer
	

	/* eJEMS2 product's major.minor.build version */
	public static String strProductMajorVersion = "4";
	public static String strProductMinorVersion = "0";
	public static String strProductBuildVersion = "0";

	/* Ejems2 main manager */ //PNS14FEB2003
	private static IEjemsMainMgrHome objIEjemsMainMgrHome = null;
	private static IEjemsMainMgrRemote objIEjemsMainMgrRemote = null;


	//RS/09/12/2005
	/*	PLEASE DO NOT DELETE THE BELOW COMMENTED CODE.
	LICENSING MODULE VARIABLES 
	//NAM290103
    private ILicenseMgrHome objILicenseMgrHome;
    private ILicenseMgrRemote objILicenseMgrRemote;
    private HashMap hmLicenseModels;*/

	static Element element = null;
    static Element elementRoot = null;

	//PNS21JULY2003
	private JLabel jlblCompanyImage; //holds the image of company as background
	JMenuItem jmniPriceLog;//saa 4/13/04

	public JLabel jlblGoldPrice;//RS/10/10/2006	
	
	JIfraSolReqMatching objJIfraSolReqMatching;//TM 1/16/2008		//TM 4/29/2008 Commented Temporary

	//UL:10/7/2008
	JIFraUnmarkHoldSolitaires objHoldSolitaire;

    public Ejems2Main(BoundObject bo)
	{

       super("Ejems3X -"+bo.getCompanyName().toString());//HRD

        this.ej2Main = this;
        this.objBO = bo;
        this.htMenuRights = bo.getModuleRights();

	
		//PNS14FEB2003 - start
        try {

			objIEjemsMainMgrHome = (IEjemsMainMgrHome)
					EJBUtil.lookupHomeInterface(EJBUtil.EJEMSMAIN_SESSION_HOME);
			objIEjemsMainMgrRemote = objIEjemsMainMgrHome.create();

	      } catch (NamingException e) {			
			Debug.print(STR_CLASS_NAME, "NamingException", "Ejems2Main Constructor", e, true);
        } catch (CreateException e) {
			Debug.print(STR_CLASS_NAME, "CreateException", "Ejems2Main Constructor", e, true);
        } catch (RemoteException e) {
			Debug.print(STR_CLASS_NAME, "RemoteException", "Ejems2Main Constructor", e, true);
        }//try-catch
		//PNS14FEB2003 - end

		//PNS02DEC2002 - start

        //open XML file as DOM object
        docUser = getUserPreferences(objBO.getLoginId()); //PNS14FEB2003 - changed to get file from the remote session

		/*Image imgFrame = Toolkit.getDefaultToolkit().getImage(
			ClassLoader.getSystemResource(ClientUtility.IMAGE_PATH+"EJems2_Frame_Logo.jpg"));

		this.setIconImage(imgFrame); */ //AST-01/11/23 code Commented for Setting new Icons to Frame.

		setCompanyIcon(objBO.getCompanyCode()); //AST- 01/06/23 Changing Company Icon 
        
		//Toolbar
        pane = getContentPane();

		//DesktopPane
        jdtpMain = new JDesktopPane();
		pane.add(jdtpMain, BorderLayout.CENTER);

		// The StaturBar object was instantiated twice.
		// Status Panel
    
	    jpnlStatus = new StatusBar(objBO,ej2Main); //daa-8/10/04
		pane.add(jpnlStatus, BorderLayout.SOUTH); //daa-8/10/04

		//Added by		JAY28/03/2003
		JDiaBullionPrice Je = new JDiaBullionPrice( "Bullion Price",ej2Main,true);
		// End of JAY28/03/2003

		jlblGoldPrice = new JLabel("Current gold price : "  + String.valueOf( "$" + Je.objETFCurrentGoldPrice.getText()));//RS/10/16/2006
		
//		RS/10/19/2006..decimal format removed

		//PNS21JULY2003
		//set company background image
		setCompanyBackgroundImage(objBO.getCompanyID());

        EventQueue waitQueue = new com.hoc.ejems2.client.util.WaitCursorEventQueue(500);
        Toolkit.getDefaultToolkit().getSystemEventQueue().push(waitQueue);
	

        /* CALL private METHOD TO CHECK THE USER LICENCES */
        /* 
		//RS/09/12/2005
		PLEASE DO NOT DELETE THE BELOW COMMENTED CODE.
		try {
            getStartupData(bo.getCompanyID(),  bo.getUserID());
        } catch (RemoteException e) {
            Debug.print(STR_CLASS_NAME, "Error fetching license information ", "Ejems2Main constructor", e, true);
            showMessageBox("Error in connecting to the server. "
                            + "\nPlease contact System Administrator. Cannot Proceed.",
                            JOptionPane.ERROR_MESSAGE);
            return;
        }//try-catch //RS/09/12/2005
		*/

			


		getLoggedInCompanyDefaultInformation(); // asl 10oct03

        /* CALL A METHOD IN A CLASS THAT WILL DISPLAY A SPLASH SCREEN */
        new Splash(ClientUtility.SPLASH_IMAGE_PATH, this, 3000);

        /**
			 This variable is referred by all modules to decide whether the dirty flag warning
			 message should be shown to the user or not. We are providing a feature wherein the
			 user can turn OFF the dirty flag warning by setting this variable value to 'false'.
			 And in all the modules the code where the question is asked whether to save the
			 record or not a check for this boolean is made. If this boolean is set then only
			 dirty flag warnings are shown.
         */
        bShowDirtyDataMessage = true;


		/* Invoking a method to add all menus and its respective menu items */
		addOptionsUnderMenus();

		/* Adding ActionListeners to required components */
		addActionListeners();
		// invisible successive separator
        // invisibleSeparator
        // this method will travers thru all the menu items.
        // It will look for successive separator. if found then it will make one instance
        //of the separator invisible.
        //And if there are no menu entry found after the last separator
        //then that separator is also made invisible.
        // rc 17 createMenuBar();
		//TM 3/5/2008 
		NetRightsModel objNetRightsModel = (NetRightsModel) htMenuRights.get(ClientUtility.MOD_SOL_RQUIREMENT);
		
		if ((objNetRightsModel.getAdd() == 1 || objNetRightsModel.getView() == 1 ||
				objNetRightsModel.getModify() == 1 || objNetRightsModel.getDelete() == 1) ) {
				findMatchingSolitaireForRequirement();//TM 1/16/2008
		}
		//TM 3/5/2008 End
       //UL:10/7/2008
        NetRightsModel objNetRightsModelUnmarkSol = (NetRightsModel) htMenuRights.get(ClientUtility.MOD_UNMARK_HOLD_SOL);
        if(objNetRightsModelUnmarkSol.getView() == 1 || objNetRightsModelUnmarkSol.getModify() == 1 )
        {
            openUnmarkHoldSolitaireModule();
            objNetRightsModelUnmarkSol = null;
        }
    } // Ejems2Main Constructor ends here

	//PNS21JULY2003 - start - added new method to read data from the database and then use it
	//company images
    private void setCompanyBackgroundImage(int intCompanyId) {
		IimageMgrHome objIimageMgrHome;
		IimageMgrRemote objIimageMgrRemote;

		ArrayList arrImages = null;
		ImageModel imgCompanyBackground = null;

        try {
            objIimageMgrHome = (IimageMgrHome) EJBUtil.lookupHomeInterface(EJBUtil.IMAGES_MGR_HOME);
            objIimageMgrRemote = objIimageMgrHome.create();
            arrImages = (ArrayList) objIimageMgrRemote.SelectImages(new Integer(intCompanyId), "COM", new Integer(intCompanyId));
        } catch (Exception e) {
            Debug.print(STR_CLASS_NAME, "Exception", "setCompanyBackgroundImage",e, true);
        }

		for (int i=0;i < arrImages.size() ; i++ ) {
			if (!((ImageModel) arrImages.get(i)).getFlagPrimaryImage().equalsIgnoreCase("Y")) { //if not primary image then take it
				imgCompanyBackground = (ImageModel) arrImages.get(i);
				break;
			}
		}
		
		//PNS21JULY2003 - Company image as background
					//PNS22JULY2003 - remove image from the desktop if exists. earlier it was done conditionally.
		if (jlblCompanyImage != null) {
			jdtpMain.remove(jlblCompanyImage);
		}

		if (imgCompanyBackground != null) {
			byte[] bufImage = imgCompanyBackground.getByteArray();
			if (imgCompanyBackground != null && bufImage != null) {
				ImageIcon iconCompanyBackground = new ImageIcon(bufImage);
				jlblCompanyImage = new JLabel(iconCompanyBackground);
				jlblCompanyImage.setBounds(0,0, imgCompanyBackground.getWidth().intValue(), imgCompanyBackground.getHeight().intValue());
				jdtpMain.setLayer(jlblCompanyImage,-1); // asl 06-08-03
				jdtpMain.add(jlblCompanyImage);
				jlblCompanyImage.add(jlblGoldPrice);//RS/10/16/2006

		}
	}


    }//setCompanyBackgroundImage
	//PNS21JULY2003 - end

  //AST-01/11/23 --> Method added For Setting Company specific Icon.
		private void setCompanyIcon(String strCdCompany) {


			Image imgFrame = Toolkit.getDefaultToolkit().getImage(
			ClassLoader.getSystemResource(ClientUtility.getEjemsClientProperties("IMAGE_"+strCdCompany+"_PATH")));
		
			this.setIconImage(imgFrame);	

		}//setCompanyIcon //AST-01/11/23


	//PNS13FEB2003 - start
    /**
     * Reads configuration value from the user's preferences. This is based on registry/INI file mechanism.
     *
     * @param sectionName   - section name
     * @param keyName       - key name
     * @param companyID     - should this be under a specific company. pass null if configuration is company independent
     * @return  - returns value associated for the given section and key. returns blank if not found.
     */
	public String ReadValue(String sectionName, String keyName, Integer companyID) {

		if (docUser == null ||
			docUser.getDocumentElement() == null ||
			sectionName.equalsIgnoreCase("") ||
			keyName.equalsIgnoreCase("")) {
			return "";
		}

		String strSearch = "";
		Element elementSearch = docUser.getDocumentElement();

        Element elementKey = null;
        try {
            if (companyID == null)
			{
                strSearch = "//user-preferences/configuration/section[@name=\"" + sectionName + "\"]/item[@name=\"" + keyName + "\"]";
            }
			else
			{
                strSearch = "//user-preferences/company[@id=" + companyID.intValue() + "]/configuration/section[@name=\"" + sectionName + "\"]/item[@name=\"" + keyName + "\"]";
            }

            elementKey = null;
            elementKey = (Element) XPathAPI.selectSingleNode(elementSearch, strSearch);
            if (elementKey == null) {
                return "";
            }
        } catch (TransformerException e) {
			Debug.print(STR_CLASS_NAME, "TransformerException  ", "ReadValue", e, true);
            return "";
        }

        return elementKey.getAttribute("value");
    }//ReadValue

    /**
    * Writes configuration value to the user's preferences. This is based on registry/INI file mechanism.
    *
    * @param sectionName   - section name
    * @param keyName       - key name
    * @param companyID     - should this be under a specific company. pass null if configuration is company independent
    * @param saveImmediately - should the changes to configuration be written to file immediately? Normally, in case, you
    *                       writing more one values, then pass "false" for all the calls to "WriteValue" except last call.
    *                       in the last call to "WriteValue", pass "true" so that changes would be written to file.
    * @return   - true if successful,
    */
    public boolean WriteValue(String sectionName, String keyName, String keyValue, Integer companyID, boolean saveImmediately) {

        Element elementRoot = null;
        Element element = null;
        String strSearch = "";

        if (docUser == null) {

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = null;
            try {
                db = dbf.newDocumentBuilder();
            } catch (ParserConfigurationException e) {
				Debug.print(STR_CLASS_NAME, "ParserConfigurationException ", "WriteValue", e, true);
            }//try-catch

            docUser = db.newDocument();

            elementRoot = docUser.createElement("user-preferences");
            elementRoot.setAttribute("code" , Ejems2Main.objBO.getLoginId());
            docUser.appendChild(elementRoot);
            //formating
            elementRoot.appendChild(docUser.createTextNode("\n\t"));

            element = docUser.createElement("default");
            element.setAttribute("company" , ""+Ejems2Main.objBO.getCompanyID());
            elementRoot.appendChild(element);
            //formating
            elementRoot.appendChild(docUser.createTextNode("\n\t"));
        } else { //user's preference found.
        }

        //pointer to the root element of the user's preference tree
        elementRoot = docUser.getDocumentElement();

        try {

            Element elementConfigurationParent = null;

            if (companyID == null) {
                elementConfigurationParent = elementRoot;
            } else {
                Element elementCompany = null;
                //search for company tag. if not found, create it.
                strSearch = "//user-preferences[@code=\""+objBO.getLoginId()+"\"]/company[@id=\"" + objBO.getCompanyID() + "\"]";
                elementCompany = (Element) XPathAPI.selectSingleNode(elementRoot, strSearch);
                if (elementCompany == null) {
                    element = docUser.createElement("company");
                    element.setAttribute("id" , ""+companyID);
                    elementRoot.appendChild(element);
                    elementCompany = element;
                    //formating
                    elementRoot.appendChild(docUser.createTextNode("\n\t"));
                }
                elementConfigurationParent = elementCompany;
            }

            Element elementConfiguration = null;
            //search for configuration tag. if not found, create it.
            strSearch = "configuration";
            elementConfiguration = (Element) XPathAPI.selectSingleNode(elementConfigurationParent, strSearch);
            if (elementConfiguration == null) {
                element = docUser.createElement("configuration");
                elementConfigurationParent.appendChild(element);
                elementConfiguration = element;
                //formating
                elementConfigurationParent.appendChild(docUser.createTextNode("\n\t"));
            }

            Element elementSection = null;
            //search for section tag. if not found, create it.
            strSearch = "section[@name=\"" + sectionName + "\"]";
            elementSection = (Element) XPathAPI.selectSingleNode(elementConfiguration, strSearch);
            if (elementSection == null) {
                element = docUser.createElement("section");
                element.setAttribute("name" , ""+sectionName);
                elementConfiguration.appendChild(element);
                elementSection = element;
                //formating
                elementConfiguration.appendChild(docUser.createTextNode("\n\t"));
            }

            Element elementItem = null;
            //search for item tag. if not found, create it.
            strSearch = "item[@name=\"" + keyName + "\"]";
            elementItem = (Element) XPathAPI.selectSingleNode(elementSection, strSearch);
            if (elementItem == null) {
                element = docUser.createElement("item");
                element.setAttribute("name" , ""+keyName);
                element.setAttribute("value" , ""+keyValue);
                elementSection.appendChild(element);
                elementItem = element;
                //formating
                elementSection.appendChild(docUser.createTextNode("\n\t"));
            } else {
                elementItem.setAttribute("value" , ""+keyValue);
            }

            if (saveImmediately) {
				saveUserPreferences(); //PNS14FEB2003 - added
            }

        } catch (TransformerException e) {
			Debug.print(STR_CLASS_NAME, "TransformerException " , "WriteValue", e, true);
            return false;
        }

        return true;
    }//WriteValue

	//PNS14FEB2003 - start
	/**
	 * This method could be used by all other modules for saving user's preferences on the server.
	 */
	public static Document getModuleXMLFile(String strModuleCode)
	{
		if (objIEjemsMainMgrRemote != null)
		{
			try
			{
				return objIEjemsMainMgrRemote.getModuleXMLFile(strModuleCode);
			}
			catch (RemoteException re)
			{
				Debug.print(STR_CLASS_NAME, "RemoteException ",  "getModuleXMLFile", re, true);
				return null;
			}
		}
		else
		{
			return null;
		}
	}//getModuleXMLFile

	/**
	 * This method could be used by all other modules for saving user's preferences on the server.
	 */
	public static void saveUserPreferences()
	{
		if (!objBO.getLoginId().equalsIgnoreCase("SYLVIA"))
		{
			setUserPreferences(objBO.getLoginId(), docUser);
		}
	}//saveUserPreferences

    /**
     * Private method called from constructor that takes the login id/code as parameter
	 * and calls remote method to get user's preferences.
     * @param  String strLoginId - login code of the user
     * @returns Document - XML Document of user's preferences
    */
    private static Document getUserPreferences(String strLoginId) {

		if (objIEjemsMainMgrRemote != null)
		{
			try
			{
				return objIEjemsMainMgrRemote.getUserPreferences(strLoginId);
			}
			catch (RemoteException re)
			{
				System.out.println(EJBUtil.EJEMS_TASK);
				Debug.print(STR_CLASS_NAME, "RemoteException  ", "getUserPreferences", re, true);
				return null;
			}
		}
		else
		{
			return null;
		}
    }//getUserPreferences()

    /**
     * Private method called from constructor that takes the login id/code as parameter
	 * and calls remote method to get user's preferences.
     * @param  String strLoginId - login code of the user
     * @returns Document - XML Document of user's preferences
    */
    private static void setUserPreferences(String strLoginId, Document docUser) {
		if (objIEjemsMainMgrRemote != null)
		{
			try
			{
				objIEjemsMainMgrRemote.setUserPreferences(strLoginId, docUser);
			}
			catch (RemoteException re)
			{
				Debug.print(STR_CLASS_NAME, "RemoteException ", "setUserPreferences", re, true);
			}
		}
    }//setUserPreferences()
	//PNS14FEB2003 - end

	private void addOptionsUnderMenus() {

        // Menu Bar
		jmnbMain = new JMenuBar();
        setJMenuBar(jmnbMain);

		// Status Panel
		pane.remove(jpnlStatus); //daa-8/16/04
		jpnlStatus = new StatusBar(objBO,ej2Main); //daa-8/10/04
		pane.add(jpnlStatus, BorderLayout.SOUTH);

        jpnlStatus.setPreferredSize(new Dimension((int) getGraphicsConfiguration().getBounds().getWidth(), 20));
        jpnlStatus.setMessage("Ready.....");
		
        setSize(WIDTH, HEIGHT);
        setResizable(true);

		//adding the main menu items to the menu bar
        jmnuFile = new JMenu("File");
        jmnuFile.setToolTipText("File");
        jmnuFile.setMnemonic('F');

        jmnuMasters = new JMenu("Masters");
        jmnuMasters.setToolTipText("Masters");
        jmnuMasters.setMnemonic('M');

		// File
        jmnuFile = new JMenu("File");
        jmnbMain.add(jmnuFile);
        jmnuFile.setMnemonic(KeyEvent.VK_F);

		jmniChangeCo = new JMenuItem("Change Company");
		jmnuFile.add(jmniChangeCo);

        jmniLogOut = new JMenuItem("Logout");
        jmnuFile.add(jmniLogOut);

        jspExit = new JSeparator();//RC JAN 11
        jmnuFile.add(jspExit);//RC JAN 11

        jmniExit = new JMenuItem("Exit");
        jmnuFile.add(jmniExit);

		// Sales
        jmnuSales = new JMenu("Sales");
        jmnbMain.add(jmnuSales);

		jmnuSales.setMnemonic(KeyEvent.VK_S);

        addMenuItem(jmnuSales, "Sales Order", ClientUtility.MOD_SO,
			"Sales Order", "JIFraSalesOrder", true);

        addMenuItem(jmnuSales, "Sales Memo Issue", ClientUtility.MOD_SMI,
			"Sales Memo Issue", "JIFraSalesMemIssue", true);

        addMenuItem(jmnuSales, "Sales Memo Return", ClientUtility.MOD_SMR,
			"Sales Memo Return", "JIFraMemoReturn", true);

        addMenuItem(jmnuSales, "Sales Invoice", ClientUtility.MOD_SI,
			"Sales Invoice", "JIFraSalesInv", true);

        addMenuItem(jmnuSales, "Sales Credit Invoice", ClientUtility.MOD_SCI,
			"Sales Credit Invoice", "JIFraSalesCrInv", true);

        addMenuItem(jmnuSales, "Sales Person Memo Issue", ClientUtility.MOD_SPMI,
			"Sales Person Memo Issue", "JIFraSalesPerMemIss", true);

        addMenuItem(jmnuSales, "Sales Person Memo Return", ClientUtility.MOD_SPMR,
			"Sales Person Memo Return", "JIFraMemoReturn", true);

		addMenuItem(jmnuSales, "Sales Memo Status", ClientUtility.MOD_SMS,
			"Sales Memo Status", "JIFraSalesMemoDetailsScan", true); //NK - 9/24/03

		// SJ14112003 - start
		addMenuItem(jmnuSales, "Return To Vendor", ClientUtility.MOD_RTV,
			"Return To Vendor", "JIFraReturnToVendor", true);
		// SJ14112003 - end

		//ss 2/24/05
		jmniSalesAnalysis = new JMenuItem("Sales Analysis");
        jmnuSales.add(jmniSalesAnalysis);

//rs/03/31/2007
		addMenuItem(jmnuSales, "Order Fulfillment", ClientUtility.MOD_ORDER_FULFILLMENT,
 		"Order Fulfillment", "JIFraOrderFulfillment", true);
//rs/03/31/2007
		
		addMenuItem(jmnuSales, "Out Of Stock Items", ClientUtility.OUT_OF_STOCK_ITEM,
			"Out Of Stock Items", "JIFraOutOfStockItems", true);//SM/10/18/2010

		// Purchase
        jmnuPurchase = new JMenu("Purchase");
        jmnbMain.add(jmnuPurchase);
        jmnuPurchase.setMnemonic(KeyEvent.VK_P);

        addMenuItem(jmnuPurchase, "Purchase Order", ClientUtility.MOD_PO,
			"Purchase Order", "JIFraPurchaseOrder", true);

        addMenuItem(jmnuPurchase, "Purchase Memo Issue", ClientUtility.MOD_PMI,
			"Purchase Memo Issue", "JIFraPurchaseMemo", true);

        addMenuItem(jmnuPurchase, "Purchase Memo Return", ClientUtility.MOD_PMR,
			"Purchase Memo Return", "JIFraMemoReturn", true);

        addMenuItem(jmnuPurchase, "Purchase Invoice", ClientUtility.MOD_PI,
			"Purchase Invoice", "JIFraPurchaseInvoice", true);

        addMenuItem(jmnuPurchase, "Purchase Credit Invoice", ClientUtility.MOD_PCI,
			"Purchase Credit Invoice", "JIFraPurchaseCreditInvoice", true);

		//saa 6/7/04
		addMenuItem(jmnuPurchase, "Inventory Requirement", ClientUtility.MOD_INVREQ,
		"Inventory Requirement", "JIFraInventoryReq", true);



/*
RS/09/12/2005
 PLEASE DO NOT DELETE THE BELOW COMMENTED CODE
		SystemComponent[] arrSysComps = (SystemComponent[]) hmLicenseModels.get("LICENSE_MODEL");
		if (arrSysComps != null)
		{
			for (int iComp =0;iComp<arrSysComps.length;iComp++) {
				SystemComponent component = arrSysComps[iComp];

				if ((component!=null) && component.getComponentCode().equalsIgnoreCase(LicenseUtility.COMPONENT_JOB_CONTRACTING)) {
*/
					// Job Contracting
					jmnuJobContracting = new JMenu("Job Contracting");
					jmnuJobContracting.setMnemonic(KeyEvent.VK_J);
					jmnbMain.add(jmnuJobContracting);

					/* Vimal - To be modified to true as last param after data insertion in Database */
					addMenuItem(jmnuJobContracting, "Job Sheet", ClientUtility.MOD_JOB_SHEET,
						"Job Sheet", "JIFraJobContracting", true);
					addMenuItem(jmnuJobContracting, "Job Bag Details", ClientUtility.MOD_JOB_BAG_DETAILS,
						"Job Bag Details", "JIFraBagDetails", true);
					addMenuItem(jmnuJobContracting, "Job Bag Movement", ClientUtility.MOD_JOB_BAG_MOVEMENT,
						"Job Bag Movement", "JIFraBagMovement", true);
					//NAM31JUL03
					//Menu added for Solitaire Component module developed at Onsite.
					addMenuItem(jmnuJobContracting, "Solitaire Components", ClientUtility.MOD_SOLITAIRE_COMPONENTS,
					"Solitaire Components", "JIFraSolitaireComponents", true);
					//NAM31JUL03
					// asl 10-21-2002 - last param to be modified to true after data insertion in Database
					jmnuJobContracting.add(new JSeparator());
					addMenuItem(jmnuJobContracting, "Repair Memo Issue", ClientUtility.MOD_REPAIR_MEMO_ISSUE,
						"Repair Memo Issue", "JIFraRepairMemoIssue", true);
					addMenuItem(jmnuJobContracting, "Repair Memo Return", ClientUtility.MOD_REPAIR_MEMO_RETURN,
						"Repair Memo Return", "JIFraRepairMemoReturn", true);
					
					
					addMenuItem(jmnuJobContracting, "Expected Return", ClientUtility.MOD_ER,
					"Expected Return", "JIFraExpectedReturn", true);//MP 24-06-2020
					// asl 10-21-2002
			//	}//if
			//}//for
		//}//...NO CHECK REQUIRED FOR LICENSING ....RS/09/12/2005



        // Inventory
        jmnuInventory = new JMenu("Inventory");
        jmnuInventory.setMnemonic(KeyEvent.VK_I);
        jmnbMain.add(jmnuInventory);

        addMenuItem(jmnuInventory, "Style Master", ClientUtility.MOD_STYLE_MASTER,
			"Style Master", "JIFraStyleMaster", true);

        addMenuItem(jmnuInventory, "Diamond Master", ClientUtility.MOD_DIAMOND_MASTER,
			"Diamond Master", "JIFraDiamondMaster", true);

        addMenuItem(jmnuInventory, "Solitaire & Stud Master", ClientUtility.MOD_SOL_MASTER,
			"Solitaire & Stud Master", "JIFraSolitaireMaster", true);
		
		addMenuItem(jmnuInventory, "Color Stone Master", ClientUtility.MOD_COLOR_STONE_MASTER,
  			"Color Stone Master", "JIFraColStMaster", true);
  
        addMenuItem(jmnuInventory, "Metal Master", ClientUtility.MOD_METAL_MASTER,
  			"Metal Master", "JIFraMetalMaster", true);
  
        addMenuItem(jmnuInventory, "Finding Master", ClientUtility.MOD_FINDING_MASTER,
  			"Finding Master", "JIFraFindingMaster", true);


        addMenuItem(jmnuInventory, "Sample Master [CAS]", ClientUtility.MOD_CASTING_MASTER,
			"Sample Master [CAS]", "JIFraCastMaster", true); //ANILSAMPLE

		addMenuItem(jmnuInventory, "Pearl Master", ClientUtility.MOD_PEARL_MASTER,
 			"Pearl Master", "JIFraPearlMaster", true);

        jmnuInventory.add(new JSeparator()); //PNS31072002

		addMenuItem(jmnuInventory, "Tray Master", ClientUtility.MOD_TRAY,
			"Tray Master", "JIFraTrayMaster", true);

        addMenuItem(jmnuInventory, "Inventory Adjustment", ClientUtility.MOD_INV_ADJUSTMENT,
			"Inventory Adjustment", "JIFraInventoryAdjustment", true);

        addMenuItem(jmnuInventory, "Inventory Mixing", ClientUtility.MOD_INV_MIXING,
			"Inventory Mixing", "JIFraInventoryMixing", true);

        addMenuItem(jmnuInventory, "Inventory Status", ClientUtility.MOD_INV_STATUS,
			"Inventory Status", "JIFraInventoryStatus", true);

        jmnuInventory.add(new JSeparator()); //PNS31072002

		addMenuItem(jmnuInventory, "Solitaire & Stud Groups", ClientUtility.MOD_SOL_STUD_GROUP,
			"Solitaire & Stud Groups", "JIFraSolitaireGroup", true);

		//ANI4/19/2005
		addMenuItem(jmnuInventory, "Solitaire Matching", ClientUtility.MOD_SOL_MATCHING,
			"Solitaire Matching", "JIFraSolitaireMatching", true);


		addMenuItem(jmnuInventory, "Parcel Creation", ClientUtility.MOD_PARCEL_CREATION,
			"Parcel Creation", "JIFraParcelCreation", true);//RS/08/09/04

		//ss 5/8/2006
		addMenuItem(jmnuInventory, "Solitaire Creation", ClientUtility.MOD_SOLITAIRE_CREATION,
		"Solitaire Creation", "JIFraSolitaireCreation", true);
		//ss 5/8/2006
        addMenuItem(jmnuInventory, "Solitaire Creation - SARIN", ClientUtility.MOD_SARIN_SOL_CREATION,
		"Solitaire Creation - SARIN", "JIFraSarinSolitaireCreation", true);//SM/5/22/2009
		// SC 05/08/2009	
		addMenuItem(jmnuInventory, "Import Certificate Data", ClientUtility.MOD_IMPORT_CERTIFICATE_DATA,
		"Import Certificate Data", "JIFraImportCertificateData", true);
		//SC 05/08/2009	
		
		//TM 1/16/2008	//TM 4/29/2008 Commented temporary
		addMenuItem(jmnuInventory, "Solitaire Requirement", ClientUtility.MOD_SOL_RQUIREMENT,
			"Solitaire Requirement", "JIfraSolReqMatching", true);

		addMenuItem(jmnuInventory, "Unmark Hold Solitaires", ClientUtility.MOD_UNMARK_HOLD_SOL,
			"Unmark Hold Solitaires", "JIFraUnmarkHoldSolitaires", true);//UL:10/7/2008

		

		// Masters
        jmnuMasters = new JMenu("Masters");
        jmnbMain.add(jmnuMasters);
        jmnuMasters.setMnemonic(KeyEvent.VK_M);

        addMenuItem(jmnuMasters, "Customer Master", ClientUtility.MOD_CUSTOMER_MASTER,
			"Customer Master", "JIFraCustomerMaster", true);

        addMenuItem(jmnuMasters, "Vendor Master", ClientUtility.MOD_VENDOR_MASTER,
			"Vendor Master", "JIFraVendorMaster", true);

        addMenuItem(jmnuMasters, "Employee Master", ClientUtility.MOD_EMPLOYEE_MASTER,
			"Employee Master", "JIFraEmployeeMaster", true);

        addMenuItem(jmnuMasters, "Sales Person Master", ClientUtility.MOD_SALESPERSON_MASTER,
			"Sales Person Master", "JIFraSalesPerMaster", true);

        addMenuItem(jmnuMasters, "Ref. Vendor Master", ClientUtility.MOD_REF_VENDOR_MASTER,
			"Ref. Vendor Master", "JIFraReferenceVendorMaster", true);//ANI3/21/2006

        jsCustomer = new JSeparator();// rc jan 11
        jmnuMasters.add(jsCustomer);

        addMenuItem(jmnuMasters, "Code Master", ClientUtility.MOD_CODE_MASTER,
			"Code Master", "JIFraCodeMaster", true);

        addMenuItem(jmnuMasters, "Inventory Code Master", ClientUtility.MOD_INV_CODE_MASTER,
			"Inventory Code Master", "JIFraInventoryCodeMaster", true);

        addMenuItem(jmnuMasters, "Jewelry Design Type", ClientUtility.MOD_JEWELRY_DESIGN_TYPE,
			"Jewelry Design Type", "JIFraJewelryDesign", true);

        jsCode = new JSeparator();
        jmnuMasters.add(jsCode);

        addMenuItem(jmnuMasters, "Payment Terms Master", ClientUtility.MOD_TERMS,
			"Payment Terms Master", "JIFraTermsMaster", true);

        addMenuItem(jmnuMasters, "Company Master", ClientUtility.MOD_COMPANY,
			"Company Master", "JIFraCompanyMaster", true);

        jsCompany = new JSeparator(); //RC 24
        jmnuMasters.add(jsCompany); //RC 24 /*rc jan 11*/

		//NAM31JUL03
		addMenuItem(jmnuMasters, "Bar Code Print", ClientUtility.MOD_BAR_CODE,
			"Bar Code Print", "JIFraBarCodePrint", true);


		jmnuMasters.add(new JSeparator());
		//NAM31JUL03

        jmnuPricing = new JMenu("Pricing");

        if (addMenuItem(jmnuPricing, "Standard Pricelist", ClientUtility.MOD_STDPL,
			"Standard Pricelist", "JIFraStdPricelist", true)) {
            bFlagPricing = true;
        }

        if (addMenuItem(jmnuPricing, "Special Pricelist", ClientUtility.MOD_SPLPL,
			"Special Pricelist", "JIFraSpPricelist", true)) {
            bFlagPricing = true;
        }

		addMenuItem(jmnuPricing, "Rap. Based Pricing", ClientUtility.MOD_RAPAPORT_PRICE,
			"Rapaport Price Update", "JIFraRapaportPriceUpdate", true);//TM 6/18/2008


        /* IF THERE IS ANY SUB-MENU ITEM UNDER PRICING ONLY THEN ADD 'PRICING' MENU */
        if (bFlagPricing) {
            jmnuMasters.add(jmnuPricing);
        }

		jmniStyleCost = new JMenu("Style Cost Setup");
  
          if (addMenuItem(jmniStyleCost, "Diamond Cost", ClientUtility.MOD_DIAMOND_COST,
  			"Diamond Cost", "JIFraDiaCost", true)) {
              bFlagStyleCostList = true;
          }
  
          if (addMenuItem(jmniStyleCost, "Diamond Setting Cost", ClientUtility.MOD_DIAMOND_SETTING,
  			"Diamond Setting Cost", "JIFraDiaSetting", true)) {
             bFlagStyleCostList = true;
          }
  
          if (addMenuItem(jmniStyleCost, "Color Stone Cost", ClientUtility.MOD_COLOR_STONE_COST,
  			"Color Stone Cost", "JIFraColStoneCost", true)) {
              bFlagStyleCostList = true;
          }
  
          if (addMenuItem(jmniStyleCost, "Color Stone Setting Cost",
  			ClientUtility.MOD_COLOR_STONE_SETTING,
  			"Color Stone Setting Cost", "JIFraColStoneSetting", true)) {
              bFlagStyleCostList = true;
          }
  
          if (addMenuItem(jmniStyleCost, "Finding Cost", ClientUtility.MOD_FINDING_COST,
  			"Finding Cost", "JIFraFindingCost", true)) {
              bFlagStyleCostList = true;
          }
  
          if (addMenuItem(jmniStyleCost, "Labor Cost", ClientUtility.MOD_LABOR_COST,
  			"Labor Cost", "JIFraLaborCost", true)) {
              bFlagStyleCostList = true;
          }
  
          if (addMenuItem(jmniStyleCost, "Metal Fineness Factor",
  			ClientUtility.MOD_METAL_FINENESS_FACTOR,
  			"Metal Fineness Factor", "JIFraMetalFinenessFactor", true)) {
              bFlagStyleCostList = true;
          }
  
          if (addMenuItem(jmniStyleCost, "Metal Conversion Factor",
  			ClientUtility.MOD_METAL_CONVERSION_FACTOR,
  			"Metal Conversion Factor", "JIFraMetalConvFactor", true)) {
              bFlagStyleCostList = true;
          }
  
          /* IF THERE IS ANY SUB-MENU ITEM UNDER 'STYLE COST SETUP' ONLY THEN ADD 'STYLE COST SETUP' MEUN */
          if (bFlagStyleCostList) {
              jmnuMasters.add(jmniStyleCost);
          }


		// Tools
        jmnuTools = new JMenu("Tools");
        jmnuTools.setMnemonic(KeyEvent.VK_T);
        jmnbMain.add(jmnuTools);

		//Taskpad - asl 29Aug03
		jmniTaskpad = new JMenuItem("Taskpad");
        jmnuTools.add(jmniTaskpad);
		jmniTaskpad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				JInternalFrame arr[] = jdtpMain.getAllFrames();

				for (int i = 0; i < arr.length; i++) {
					JInternalFrame jj = (JInternalFrame) arr[i];
					if (jj.getTitle().equalsIgnoreCase("Taskpad")) {
						jj.toFront();
						try {
							jj.setSelected(true);
							jj.setIcon(false);
							return;
						} catch (java.beans.PropertyVetoException exc) {
							Debug.print(STR_CLASS_NAME, "java.beans.PropertyVetoException", "jmniTaskpad action listener", exc, true);
						} //try-catch
					}//if-else
				}//for

				InternalFrameTemplate ift = new JIFraTaskPad("Taskpad", ej2Main);
				addInternalFrame(ift,getModuleId(ClientUtility.MOD_TASKPAD));//SM/7/14/2010
				return;
			}
		});

		addMenuItem(jmnuTools, "Calculator", ClientUtility.MOD_CALCULATER, "eJems Calculator", "JIFraCalculator", true);//SM/7/14/2010

		 addMenuItem(jmnuTools, "Message Center", ClientUtility.MOD_MSG_CNTR,
			"Message Center", "JIFraMessageCenter", true); //daa 5/25/04
        JMenuItem jmniOptions = new JMenuItem("Options");
        jmnuTools.add(jmniOptions);


        addMenuItem(jmnuTools, "EDI", ClientUtility.MOD_EDI,
			"EDI", "JIFraEDI", true);

		//Added by JAY28/03/2003.
		//jmniBullionPrice
		addMenuItem(jmnuTools, "Bullion Price", ClientUtility.MOD_PRICE,
			"Bullion Price", "JDiaBullionPrice", true);
		//End by JAY28/03/2003.

		//added by Vinod on 28Aug 2003
		addMenuItem(jmnuTools, "Rapaport", ClientUtility.MOD_RAPAPORT,"Rapaport Setup", "JIFraRapaport", true); //ss 11/24/2005

		addMenuItem(jmnuTools , "Set Gold Surcharge",ClientUtility.MOD_GOLDSURCHARGE ,"Gold Surcharge","JIFraSetGoldSurcharge",true);//ss 1/7/05 //ss 11/24/2005
		addMenuItem(jmnuTools , "Order Creation",ClientUtility.MOD_ORDER_CREATION ,"Order Creation","JIFraSalesOrderCreation",true);//ss 2/21/2007
        addMenuItem(jmnuTools , "Over Stock Order Creation",ClientUtility.MOD_ORDER_CREATION ,"Over Stock Order Creation","JIFraOverStockSOCreation",true);//SC::12/9/2010

		addMenuItem(jmnuTools , "Payment Details",ClientUtility.MOD_PAYMENT_DETAILS,"Payment Details","JIFraPayments",true);

		//UL:4/3/2008		//TM 4/29/2008 Commented temporary //UL:5/13/2008 uncomment the below code
		addMenuItem(jmnuTools , "Item Association",ClientUtility.MOD_ITEM_ASSOCIATION,"Item Association","JIFraItemAssociation",true);

		addMenuItem(jmnuTools, "Quotation", ClientUtility.MOD_QUOTE,"Quote", "JIFraQuote", true);//SM/7/23/2008

		addMenuItem(jmnuTools , "Overdue Transaction Tracking",ClientUtility.MOD_OVER_DUE_TRANSACTION,"Overdue Transaction Tracking","JIFraOverDueTransaction",true);//UL:9/15/2008
          // SC::08/17/2009 ----Start	
		addMenuItem(jmnuTools, "JBT Ratings Update", ClientUtility.MOD_JBT_RATINGS_UPDATE,//SC::10/08/2009
		"JBT Ratings Update", "JIFraJbtRatingsUpdate", true);
		// SC::08/17/2009----End	
		addMenuItem(jmnuTools , "Customer Overdue Tracking",ClientUtility.MOD_CUSTOMER_OVER_DUE,"Customer Overdue Tracking","JIFraCustomerOverDueTracking",true);//SM/9/16/2009

		addMenuItem(jmnuTools , "BELK Sales Order",ClientUtility.MOD_BELK_SALES_ORDER,"BELK Sales Order","JIFraImportBelkPO",true);//SM/11/16/2009

		addMenuItem(jmnuTools , "Bulk Sales Invoice",ClientUtility.MOD_BULK_INVOICE,"Bulk Sales Invoice","JIFraBulkSalesInvoice",true);//MP 20/05/2019

		addMenuItem(jmnuTools , "Invoice Creation",ClientUtility.MOD_INVOICE_CREATION,"Invoice Creation","JIFraInvoiceCreation",true);//MP 6/08/2019

		// Security
        jmnuSecurity = new JMenu("Security");
        jmnuSecurity.setMnemonic(KeyEvent.VK_E);
        jmnbMain.add(jmnuSecurity);

        addMenuItem(jmnuSecurity, "Change Password", ClientUtility.MOD_CHANGE_PASSWORD,
			"Security - Change Password", "JIFraChangePassword", true);

        addMenuItem(jmnuSecurity, "User Management", ClientUtility.MOD_USER,
			"Security - User Management", "JIFraUserMgmt", true);

        addMenuItem(jmnuSecurity, "Group Management", ClientUtility.MOD_GROUP,
			"Security - Group Management", "JIFraGroupMgmt", true);

        addMenuItem(jmnuSecurity, "Rights Management", ClientUtility.MOD_RIGHTS,
			"Security - Rights Management", "JIFraRightsMgmt", true);

        addMenuItem(jmnuSecurity, "Net User Rights", ClientUtility.MOD_NETRIGHTS,
			"Security - Net User Rights", "JIFraNetUserRights", true);

        addMenuItem(jmnuSecurity, "Unlocking", ClientUtility.MOD_UNLOCK,
			"Unlocking", "JIFraUnlock", true);
		//NAM31122002
		addMenuItem(jmnuSecurity, "Module Master", ClientUtility.MODULE_MASTER,
			"Module Master", "JIFraModuleMaster", true);

		addMenuItem(jmnuSecurity, "Price Log", ClientUtility.PRICE_LOG,
			"Price Log", "JIFraPriceLog", true);//saa 4/13/04

		addMenuItem(jmnuSecurity, "Category Restriction", ClientUtility.MOD_CATEGORY_RESTRICTION,
			"Category Restriction", "JIFraCategoryRestriction", true); //SC::09/19/2009

		addMenuItem(jmnuSecurity, "Module Log", ClientUtility.MODULE_LOG,
			"Module Log", "JIFraModuleLog", true);//SM/7/14/2010


		// Scans
        jmnuScans = new JMenu("Scans");
        jmnbMain.add(jmnuScans);
        jmnuScans.setMnemonic(KeyEvent.VK_C);

        addMenuItem(jmnuScans, "Customer Scan", ClientUtility.MOD_CUSTOMER_SCAN,
			"Customer Scan", "JIFraCustomerScan", true);

        addMenuItem(jmnuScans, "Vendor Scan", ClientUtility.MOD_VENDOR_SCAN,
			"Vendor Scan", "JIFraVendorScan", true);

        jmnuSalesScans = new JMenu("Sales Scan");

        if (addMenuItem(jmnuSalesScans, "Sales Order Scan", ClientUtility.MOD_SO_SCAN,
			"Sales Order Scan", "JIFraSalesOrdScans", true)) {
            bFlagSalesScan = true;
        }

        if (addMenuItem(jmnuSalesScans, "Sales Memo Scan", ClientUtility.MOD_SM_SCAN,
			"Sales Memo Scan", "JIFraSalesMemoScans", true)) {
            bFlagSalesScan = true;
        }

		//PSK 6/18/07 - Menu item for salesperson memo scan
		 if (addMenuItem(jmnuSalesScans, "Sales Person Memo Scan", ClientUtility.MOD_SPM_SCAN,
			"Sales Person Memo Scan", "JIFraSalesPersonMemoScans", true)) {
            bFlagSalesScan = true;
        }

		/* IF THERE IS ANY SUB-MENU ITEM UNDER 'SALES SCAN' ONLY THEN ADD 'SALES SCAN' MEUN */
        if (bFlagSalesScan) {
            jmnuScans.add(jmnuSalesScans);
        }

        // Purchase Scans
        jmnuPurScans = new JMenu("Purchase");

        if (addMenuItem(jmnuPurScans, "Purchase Order Scan", ClientUtility.MOD_PO_SCAN,
			"Purchase Order Scan", "JIFraPurchaseOrderScan", true)) {
            bFlagPurchaseScan = true;
        }

        if (addMenuItem(jmnuPurScans, "Purchase Memo Scan", ClientUtility.MOD_PM_SCAN,
			"Purchase Memo Scan", "JIFraPurchaseMemoScan", true)) {
            bFlagPurchaseScan = true;
        }

        /* IF THERE IS ANY SUB-MENU ITEM UNDER 'PURCHASE SCAN' ONLY THEN ADD 'PURCHASE SCAN' MEUN */
        if (bFlagPurchaseScan) {
            jmnuScans.add(jmnuPurScans);
        }

		//Inventory
        jmnuInventoryScans = new JMenu("Inventory");

        if (addMenuItem(jmnuInventoryScans, "Style Scan", ClientUtility.MOD_STYLE_SCAN,
			"Style Scan", "JIFraStyleScan", true)) {
            bFlagInvScan = true;
        }

        if (addMenuItem(jmnuInventoryScans, "Diamond Scan", ClientUtility.MOD_DIAMOND_SCAN,
			"Diamond Scan", "JIFraDiamondScan", true)) {
            bFlagInvScan = true;
        }

        if (addMenuItem(jmnuInventoryScans, "Solitaire Scan", ClientUtility.MOD_SOL_SCAN,
			"Solitaire Scan", "JIFraSolScan", true)) {
            bFlagInvScan = true;
        }

		if (addMenuItem(jmnuInventoryScans, "Sample Scan", ClientUtility.MOD_CASTING_SCAN,
			"Sample Scan", "JIFraSampleScan", true)) {
            bFlagInvScan = true;
        }//TM 1/5/2007

		if (addMenuItem(jmnuInventoryScans, "Finding Scan", ClientUtility.MOD_FINDING_SCAN,
			"Finding Scan", "JIFraFindingScan", true)) {
            bFlagInvScan = true;
        }

        /* IF THERE IS ANY SUB-MENU ITEM UNDER 'INVENTORY SCAN' ONLY THEN ADD 'INVENTORY SCAN' MEUN */
        if (bFlagInvScan) {
            jmnuScans.add(jmnuInventoryScans);
        }

		// Reports
        jmnuReports = new JMenu("Reports");
        jmnbMain.add(jmnuReports);
        jmnuReports.setMnemonic('R');
        addMenuItem(jmnuReports, "Reports", ClientUtility.MOD_REPORTS,
			"Reports", "JIFraReports", true);

		/* psk 3/23/2007
		//Old menu order removed

        addMenuItem(jmnuReports, "FA Ageing Report", ClientUtility.MOD_FAREPORTS,
			"FA Reports", "JIFrmAgedInvoice", true);

//		Added by jitendra for customer statement report call from ejems2 as on 5/25/2005
        addMenuItem(jmnuReports, "Customer Statement Report", ClientUtility.MOD_FACUSTSTMT,
			"FA Customer Statement Reports", "JIFrmCustomerStatement", true);

//		Added by jitendra for check information report call from ejems2 as on 11/21/2006
        addMenuItem(jmnuReports, "FA Check Information Report", ClientUtility.MOD_FACHECKINFOREPORTS,
			"FA Check Information Reports", "JIFrmCheckInformationReport", true);

		 addMenuItem(jmnuReports, "Closeout Report", ClientUtility.MOD_CLOSEOUTREPORT,
			"Closeout Reports", "JIFrmCloseoutReport", true);
		psk 3/23/2007 end
		*/
		
		//psk 3/23/2007
		//New report menu order
		JMenu jmnuFinanceReports = new JMenu("Finance Reports");

		addMenuItem(jmnuFinanceReports, "FA Ageing Report", ClientUtility.MOD_FAREPORTS,
			"FA Reports", "JIFrmAgedInvoice", true);

        addMenuItem(jmnuFinanceReports, "Customer Statement Report", ClientUtility.MOD_FACUSTSTMT,
			"FA Customer Statement Reports", "JIFrmCustomerStatement", true);

        addMenuItem(jmnuFinanceReports, "FA Check Information Report", ClientUtility.MOD_FACHECKINFOREPORTS,
			"FA Check Information Reports", "JIFrmCheckInformationReport", true);

		jmnuReports.add(jmnuFinanceReports);

/*   TM 5/7/2007 */
//PSK 5/22/07 Uncommented the menu item.
		JMenu jmnuOtherReports = new JMenu("Others Reports");

		addMenuItem(jmnuOtherReports, "Closeout Report", ClientUtility.MOD_CLOSEOUTREPORT,
			"Closeout Reports", "JIFrmCloseoutReport", true);

		addMenuItem(jmnuOtherReports, "Stock List", ClientUtility.MOD_STOCK_LIST,
			"Stock List", "JIFraStockList", true);//TM 4/23/2008

		addMenuItem(jmnuOtherReports, "RJO EDI", ClientUtility.MOD_RJO_EDI,
			"RJO EDI File Generation", "JIFraRJOEDIGeneration", true);//PSK 6/6/08

		addMenuItem(jmnuOtherReports, "PI/PM List", ClientUtility.MOD_PI_PM_LIST,
			"PI/PM Item List", "JIFraPIPMItemlist", true);
		
		
		jmnuReports.add(jmnuOtherReports);

		//psk 3/23/2007 end

		// Window
        jmnuWindow = new JMenu("Window");
        jmnuWindow.setMnemonic(KeyEvent.VK_W);
        jmnbMain.add(jmnuWindow);
        jmniWindowCascade = new JMenuItem("Cascade");
        jmniWindowCloseAll = new JMenuItem("CloseAll");

		jmnuWindow.addMenuListener(new MenuListener (){
            public void menuCanceled(MenuEvent e) {
            }
            public void menuDeselected(MenuEvent e) {
            }
            public void menuSelected(MenuEvent e) {
                addToWindowList();
            }

        });

        // Help
        jmnuHelp = new JMenu("Help");
        jmnuHelp.setMnemonic(KeyEvent.VK_H);
        jmnbMain.add(jmnuHelp);

        jsHelp = new JSeparator();
        jmnuHelp.add(jsHelp); /*rc jan 11*/

        addMenuItem(jmnuHelp, "About Ejems3X", ClientUtility.MOD_HELP, "About Ejems3X", "JAbout", false); //PNS30072002 //SM/7/19/2010

        /* Disable menu items which are not allowed for this user. */
        extractRights(htMenuRights); //will have to be checked later .... PRAKASH

        checkSeparator();

        setVisible(true);
// asl 1/22/04
		String strOpenTaskpad = ReadValue("taskpad", "open", null); //read if taskpad should be opened or not on the startup
		if (strOpenTaskpad.equals("") || strOpenTaskpad.equals("1")) { //if user preference not found then assume open
			jmniTaskpad.doClick(); //Taskpad - asl 29Aug03
		}
	} // addOptionsUnderMenus


	private void addActionListeners() {

        this.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                /* LISTENER FOR F7 KEY TO OPEN THE CALCULATOR */
				if (e.getKeyCode() == KeyEvent.VK_F7) {
                    try {
						JInternalFrame arr[] = jdtpMain.getAllFrames();

						if (arr.length == 0) {

							JIFraCalculator jiFraCalc = new JIFraCalculator("eJems Calculator", ej2Main);
							jiFraCalc.setVisible(true);
							addInternalFrame(jiFraCalc,getModuleId(ClientUtility.MOD_CALCULATER));//SM/7/14/2010
							return;
						}

                        for (int i = 0; i < arr.length; i++) {

							JInternalFrame jj = (JInternalFrame) arr[i];

                            if (jj.getTitle().equalsIgnoreCase("eJems Calculator")) {

                                jj.toFront();
                                try {

                                    jj.setSelected(true);
                                    jj.setIcon(false);
                                    return;

								} catch (java.beans.PropertyVetoException exc) {
									Debug.print(STR_CLASS_NAME, "java.beans.PropertyVetoException ", "addActionListeners()", exc, true);
                                } //try-catch
							}//if-else
                        }//for

						JIFraCalculator jiFraCalc = new JIFraCalculator("eJems Calculator", ej2Main);
						addInternalFrame(jiFraCalc,getModuleId(ClientUtility.MOD_CALCULATER));//SM/7/14/2010
						jiFraCalc.setVisible(true);
						return;

                    } catch (Exception e1) {
						Debug.print(STR_CLASS_NAME, null, "addActionListeners", e1, true);
                    }//try-catch
                }//if

				//Taskpad - asl 29Aug03
				if (e.getKeyCode() == KeyEvent.VK_F12) {
					jmniTaskpad.doClick();
				}

            }//keyPressed
        });


//ss 2/24/05
/* Adding an action Listener to open a website*/
		jmniSalesAnalysis.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {

			NetRightsModel objNetRightsModel = (NetRightsModel) htMenuRights.get(ClientUtility.MOD_SALES_ANAL);
			if ((objNetRightsModel.getAdd() == 1 || objNetRightsModel.getView() == 1 ||
				objNetRightsModel.getModify() == 1 || objNetRightsModel.getDelete() == 1) ) {
					try
					{
						Runtime.getRuntime().exec("cmd /C start IEXPLORE.EXE  " + ClientUtility.strSalesAnalWebSite);
					}catch(IOException ie){
						Debug.print(STR_CLASS_NAME, "IOException ", "jmniSalesAnalysis action performed", ie, true);
					}
                }else{
					JOptionPane.showMessageDialog(null, "Not Available", "eJems2", JOptionPane.ERROR_MESSAGE);
				}
            }
        });

		/* Adding ActionListener to Logout option */
		jmniLogOut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if (closeApplication()) {
                    dispose();
                    new JFraLogin();
                }
            }
        });

		/* Adding ActionListener to Change Company option */
		jmniChangeCo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				processLoggingInDifferentCompany();
//RS/10/17/2006
			JDiaBullionPrice JDBP = new JDiaBullionPrice( "Bullion Price",ej2Main,true);
			jlblGoldPrice.setText("Current gold price : "  + String.valueOf( "$" + JDBP.objETFCurrentGoldPrice.getText()));//RS/10/16/2006
			//		RS/10/19/2006..decimal format removed

			//UL:10/7/2008
			NetRightsModel objNetRightsModelUnmarkSol = (NetRightsModel) htMenuRights.get(ClientUtility.MOD_UNMARK_HOLD_SOL);
			if(objNetRightsModelUnmarkSol.getView() == 1 || objNetRightsModelUnmarkSol.getModify() == 1 )//UL:10/8/2008
			{
				openUnmarkHoldSolitaireModule();
				objNetRightsModelUnmarkSol = null;
			}

		}
		});

		/* Adding ActionListener to Exit option */
        jmniExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if (closeApplication()) {
                    dispose();
                    System.exit(0);
                }
            }
        });

		/* Window Closing Listener */
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                if (closeApplication()) {
                    dispose();
                    System.exit(0);
                }
            }
        });
	} // addActionListeners

    /*
	//RS/09/12/2005
	PLEASE DO NOT DELETE THE BELOW COMMENTED CODE
     * Private method called from constructor that takes the CompanyId and the User Id and
     * returns back the Collection of startup related information like the available
     * licenses.
     * @param  int Company Id
     * @param int User Id
     * @returns void
     * @exception RemoteException if the unable to connect to the server.
    
    private void getStartupData(int iCompanyID,  int iUserId)
                 throws RemoteException {

        try {
			
			//NAM290103
            objILicenseMgrHome = (ILicenseMgrHome)
                    EJBUtil.lookupHomeInterface(LicenseUtility.LICENSE_SESSION_HOME);
            objILicenseMgrRemote = objILicenseMgrHome.create();

            hmLicenseModels = objILicenseMgrRemote.getStartupInformation(iCompanyID, iUserId);
        } catch (NamingException e) {
			Debug.print(STR_CLASS_NAME, "NamingException ", "getStartupData", e, true);
            throw new RemoteException(e.getMessage());
        } catch (CreateException e) {
			Debug.print(STR_CLASS_NAME, "CreateException ", "getStartupData",e, true);
            throw new RemoteException(e.getMessage());
        }//try-catch
    }//getStartupData()

    **
     * The individual modules will call this method to get the license related information
     * that particular module. The method returns ComponentLicenseModel if the license
     * was present for this module. Else it will return null.
     * @param String Compnenet License Code
     * @returns ComponentLicenseModel
   
    public SystemComponent getLicenseInfo(String strCompnenetLicenseCode) {

        SystemComponent component = null;
		boolean blnFound = false;
		if(null!=(SystemComponent[]) hmLicenseModels.get("LICENSE_MODEL"))
		{
			SystemComponent[] arrSysComps = (SystemComponent[]) hmLicenseModels.get("LICENSE_MODEL");
			for (int iComp =0;iComp<arrSysComps.length;iComp++) {

				component = arrSysComps[iComp];
				if ((component!=null) && component.getComponentCode().equalsIgnoreCase(strCompnenetLicenseCode)) {
					blnFound = true;
					break;
				}
			}//FOR()
		}
		if (blnFound)
		{
			return component;
		}
		else
		{
	        return null;
		}

    }//getLicenseInfo()...RS/09/12/2005
*/

     public boolean addMenuItem(JMenuItem jmniParent, final String strMenuText,
			final String strModuleCD, final String strTitle, final String strFrameName,
            boolean bSecurityReqd) {

        boolean bAdded = false;
        JMenuItem jmni;

        NetRightsModel objNetRightsModel = null;

		//PNS27072002: added try catch block

		try	{

			if (bSecurityReqd) {

				objNetRightsModel = (NetRightsModel) htMenuRights.get(strModuleCD);
				

			} else {

				objNetRightsModel = new NetRightsModel();
				objNetRightsModel.setAdd(1);
				objNetRightsModel.setModify(1);
				objNetRightsModel.setView(1);
				objNetRightsModel.setDelete(1);
				
			}			
			
			//UL:5/7/2008 add condition to check the payment details
			//UL:5/7/2008 also change the condition for parcel. It was wrong syntex
			//UL:5/13/2008 added condition for item association
			if ((objNetRightsModel.getAdd() == 1 || objNetRightsModel.getView() == 1 ||
				objNetRightsModel.getModify() == 1 || objNetRightsModel.getDelete() == 1) &&  !(strModuleCD.equalsIgnoreCase("PARCEL")) && !(strModuleCD.equalsIgnoreCase("PAYMENT_DETAILS")) && 
				!(strModuleCD.equalsIgnoreCase("ITEM_ASSOCIATION")) && !(strModuleCD.equalsIgnoreCase("UNMARK_HOLD_SOL"))) {//RS/08/09/04-added the 2nd condition in this statement. //UL:10/8/2008

				bAdded = true;

				jmni = new JMenuItem(strMenuText);
				jmniParent.add(jmni);

				jmni.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent ae) {
						try {
							Class cls;
							if(strFrameName != "JIFraSalesOrderCreation"){
								cls = Class.forName("com.hoc.ejems2.client.frames." + strFrameName);
							}else{
								cls = Class.forName("com.hoc.ejems2.client.frames.ordercreation." + strFrameName); //ss 2/21/2007
							}

							Constructor cst;
							Object[] objParam;

							if (strModuleCD.equals("SMR") || strModuleCD.equals("PMR") ||
								strModuleCD.equals("SPMR")) {

								cst = cls.getConstructor(new Class[]{String.class, String.class, Ejems2Main.class});
								objParam = new Object[]{strTitle, strModuleCD , ej2Main};

							} else if (strModuleCD.equals("STATUS")) {

								cst = cls.getConstructor(new Class[]{String.class, String.class, String.class, String.class, Ejems2Main.class});
								objParam = new Object[]{strTitle, "M","","", ej2Main};

							}
							else if(strModuleCD.equals("SM_STATUS")) //NK - 9/24/03
							{
								cst = cls.getConstructor(new Class[]{String.class, Integer.class, Integer.class,Ejems2Main.class});
								objParam = new Object[]{strTitle,null,null,ej2Main};
							}else {
								cst = cls.getConstructor(new Class[]{String.class, Ejems2Main.class});
								objParam = new Object[]{strTitle, ej2Main};

							}
							//PNS21JULY2003 - Added to take care of bullion price module - it is a dialog and not an internal frame template
							if (strModuleCD.equals(ClientUtility.MOD_PRICE)) {
								cst.newInstance(objParam);
								//As Solitaire reqiuirement is dialog box	TM 1/16/2008 
							} /*else if(strModuleCD.equals(ClientUtility.MOD_SOL_RQUIREMENT)){
								cst.newInstance(objParam);
							}*/else{
								InternalFrameTemplate ift = (InternalFrameTemplate)	cst.newInstance(objParam);
								addInternalFrame(ift,getModuleId(strModuleCD));//SM/7/14/2010
							}

							setVisible(true);

						} catch (ClassNotFoundException cls) {
							Debug.print(STR_CLASS_NAME, "ClassNotFoundException ", "jmni actionPerformed", cls,true);
							JOptionPane.showMessageDialog(null, "Not Available", "eJems2", JOptionPane.ERROR_MESSAGE);
						} catch (Exception e) {
							String strMsg = "Could not create .... " + strMenuText + " - " + strFrameName;
							Debug.print(STR_CLASS_NAME, strMsg, "jmni actionPerformed",e,true);
						}
					}
				});
			}
			else if (strModuleCD.equalsIgnoreCase("PAYMENT_DETAILS") || strModuleCD.equalsIgnoreCase("ITEM_ASSOCIATION") || strModuleCD.equalsIgnoreCase("UNMARK_HOLD_SOL"))//UL:5/7/2008 starts from here //UL:5/13/2008
			{
				if(objNetRightsModel.getView() == 1 || objNetRightsModel.getModify() == 1)//UL:10/8/2008
				{
						bAdded = true;

						jmni = new JMenuItem(strMenuText);
						jmniParent.add(jmni);

						jmni.addActionListener(new ActionListener() {

			
							public void actionPerformed(ActionEvent ae) {
								try{
										
										Class cls = Class.forName("com.hoc.ejems2.client.frames." + strFrameName);

										Constructor cst;
										Object[] objParam;

										cst = cls.getConstructor(new Class[]{String.class, Ejems2Main.class});
										objParam = new Object[]{strTitle, ej2Main};

										InternalFrameTemplate ift = (InternalFrameTemplate)	cst.newInstance(objParam);
										addInternalFrame(ift,getModuleId(strModuleCD));//SM/7/14/2010
										
										
										setVisible(true);
									}catch (ClassNotFoundException cls) {
										Debug.print(STR_CLASS_NAME, "ClassNotFoundException ", "jmni actionPerformed",cls,true);
										JOptionPane.showMessageDialog(null, "Not Available", "eJems2", JOptionPane.ERROR_MESSAGE);
									}
									catch (Exception e) {
										String strMsg = "Could not create .... " + strMenuText + " - " + strFrameName;
										Debug.print(STR_CLASS_NAME, strMsg, "jmni actionPerformed",e,true);
									}
								}
								
						});
				}
			}//else end's here //UL:5/7/2008
			else if (objNetRightsModel.getAdd() == 1 )//RS/08/09/04 starts from here
			{
				
						bAdded = true;

						jmni = new JMenuItem(strMenuText);
						jmniParent.add(jmni);

						jmni.addActionListener(new ActionListener() {


							public void actionPerformed(ActionEvent ae) {
								try{
										
										Class cls = Class.forName("com.hoc.ejems2.client.frames." + strFrameName);
	
										Constructor cst;
										Object[] objParam;

										cst = cls.getConstructor(new Class[]{String.class, Ejems2Main.class});
										objParam = new Object[]{strTitle, ej2Main};

										InternalFrameTemplate ift = (InternalFrameTemplate)	cst.newInstance(objParam);
										addInternalFrame(ift,getModuleId(strModuleCD));//SM/7/14/2010
										
										
										setVisible(true);
									}catch (ClassNotFoundException cls) {
										Debug.print(STR_CLASS_NAME, "ClassNotFoundException ", "jmni actionPerformed",cls,true);
										JOptionPane.showMessageDialog(null, "Not Available", "eJems2", JOptionPane.ERROR_MESSAGE);
									}
									catch (Exception e) {
										String strMsg = "Could not create .... " + strMenuText + " - " + strFrameName;
										Debug.print(STR_CLASS_NAME, strMsg, "jmni actionPerformed",e,true);
									}
							}
				
				});
			}//else end's here //RS/08/09/04
		}

		catch (NullPointerException npe) { //PNS27072002 null pointer occurs if the menu entry is not found in the security hashtable

			//PNS27072002
			//security data not available, so do not add menu item.
			Debug.print(STR_CLASS_NAME, "NullPointerException ", "actionPerformed", npe, true);
		}

        return bAdded;
    }


    /**
     * This method is called by all Client(extending InternalFrameTemplates).
     * It retrieves the rights assigned for this user for the module he has
     * selected form the menu and then return the NetRoghtsModel having all rights.
     *
     * @param String ModuleCode
     * @param
     * @param
     * @returns
     * @exception
     */
    public NetRightsModel checkUserRights(String strModuleCd) {

        NetRightsModel objNRModel = (NetRightsModel) htMenuRights.get(strModuleCd);
        return objNRModel;

    }//checkUserRights()

	public Integer getModuleId(String strModuleCd) {
        NetRightsModel objNRModel = (NetRightsModel) htMenuRights.get(strModuleCd);
        return objNRModel.getModuleId();

    }//getModuleId() //SM/7/14/2010


//rc 15 method to extract the module rights fro the user logged into the specific company
    private void extractRights(Hashtable htMenuRights)
    {
        Vector vecCd = new Vector();

        for (Enumeration enumKeys = htMenuRights.keys(); enumKeys.hasMoreElements();)
        {
            String strModCd = (String) enumKeys.nextElement();
            vecCd.add(strModCd);
        }

        for (int i = 0; i < vecCd.size(); i++)
        {
            NetRightsModel objNetRightsModel = (NetRightsModel) htMenuRights.get(vecCd.get(i));
            vecKeys.add(objNetRightsModel);
        }

    }
// rc end

//----------------------------------
    /** Check Separators */
    private void checkSeparator()
    {
        for (int i = 0; i < jmnbMain.getMenuCount(); i++)
        {
            Component[] arrMenuComp = jmnbMain.getMenu(i).getMenuComponents(); // masters at menu bar index = 5
            int intLastMenuItem = -1;
            for (int j = 0; j < arrMenuComp.length; j++)
            {
                //Prakash: removed loops
                if (arrMenuComp[j].isVisible())
                {
                    if (arrMenuComp[j] instanceof JSeparator)
                    {
                        if (intLastMenuItem == -1)
                        {
                            //first visible menu item is separator so hide it
                            ((JSeparator) arrMenuComp[j]).setVisible(false);
                            ((JSeparator) arrMenuComp[j]).setEnabled(false);
                        }
                        else
                        {
                            if (arrMenuComp[intLastMenuItem] instanceof JSeparator)
                            {
                                //if previous visible menu item was also a separator then hide
                                ((JSeparator) arrMenuComp[intLastMenuItem]).setVisible(false);
                                ((JSeparator) arrMenuComp[intLastMenuItem]).setEnabled(false);
                            }
                        }
                    }
                    intLastMenuItem = j;
                }
            }

            if (intLastMenuItem != -1)
            {
                if (arrMenuComp[intLastMenuItem] instanceof JSeparator) //if last menu item is a separator then suppress that also
                {
                    ((JSeparator) arrMenuComp[intLastMenuItem]).setVisible(false);
                    ((JSeparator) arrMenuComp[intLastMenuItem]).setEnabled(false);
                }
            }
        }
    }
//----------------------------------

    public boolean closeApplication()
    {
        c = jdtpMain.getAllFrames();
        JInternalFrame ifs[] = jdtpMain.getAllFrames();

        if (c.length != 0) {
			
            int ians;
			ians = ClientUtility.showConfirmationMessage("One or more frames are open.\n Are you sure you want to exit?",JOptionPane.WARNING_MESSAGE,JOptionPane.YES_NO_OPTION, "EJEMS! Close Application");
			
			if (ians == 0) {
                for (int i = 0; i < c.length; i++) {
                    if ((ifs[i].getClass()).isInstance(c[i])) {
						
						//TM 3/24/2006 Start
						//when more frame are open and user want to exit as it is.
						IUserLoginMgrHome objIUserLoginMgrHome = null;
						IUserLoginMgrRemote objIUserLoginMgrRemote = null;

						objBO.setAction("OUT");

                        try {
							((JInternalFrame) c[i]).setVisible(true);
                            ((JInternalFrame) c[i]).doDefaultCloseAction();

							/* Getting Home Interface for UserLoginMgr */
							objIUserLoginMgrHome = (IUserLoginMgrHome) EJBUtil.lookupHomeInterface(EJBUtil.LOGIN_MGR_HOME);
							
							/* Getting Remote Interface for UserLoginMgr */
							objIUserLoginMgrRemote = objIUserLoginMgrHome.create();

							objIUserLoginMgrRemote.insertUserLogInfo(objBO);
							
                        } catch (Exception e) {
							Debug.print(STR_CLASS_NAME, null, "closeApplication",e, true);
                        }
						//TM 3/24/2006 End  
                    }
                }
					
	             //[Licensing] Clear the info from Application Registry
                //-------------START ashish for application registry----------------------
//                JIFraAppliationRegistry.removeEntryFromApplicationRegistry(objBO.getLoginId()); //PLEASE DO NOT REMOVE - RS/10/11/2005
                //-------------END ashish for application registry----------------------
                return true;	//user decided to close application

            } else {

				return false;	//user decided not close application

			}
        }

		 //[Licensing] Clear the info from Application Registry
                //-------------START ashish for application registry----------------------
//                JIFraAppliationRegistry.removeEntryFromApplicationRegistry(objBO.getLoginId()); //PLEASE DO NOT REMOVE - RS/10/11/2005
                //-------------END ashish for application registry----------------------

			
			//TM 3/24/2006 Start
			//Normal exit or logout. 
			IUserLoginMgrHome objIUserLoginMgrHome = null;
			IUserLoginMgrRemote objIUserLoginMgrRemote = null;

			objBO.setAction("OUT");

			try {

				/* Getting Home Interface for UserLoginMgr */
				objIUserLoginMgrHome = (IUserLoginMgrHome) EJBUtil.lookupHomeInterface(EJBUtil.LOGIN_MGR_HOME);
				
				/* Getting Remote Interface for UserLoginMgr */
				objIUserLoginMgrRemote = objIUserLoginMgrHome.create();

				objIUserLoginMgrRemote.insertUserLogInfo(objBO);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			//TM 3/24/2006 End
			
			return true;	//no frames are open so allow application to close

	} // closeApplication


    public void addInternalFrame(JInternalFrame jif,Integer intModuleId)//SM/7/14/2010
    {

        	//SM/7/14/2010

			ILogMgrHome objILogMgrHome;
			ILogMgrRemote objILogMgrRemote;
			ModuleLogModel objModuleLogModel = null;
			ModuleLogModel objSaveModuleLogModel = createModuleLogModel(intModuleId, "open", new ModuleLogModel());
			try {
				
				objILogMgrHome = (ILogMgrHome)EJBUtil.lookupHomeInterface(EJBUtil.LOG_MGR_HOME);
				objILogMgrRemote = objILogMgrHome.create();
				objModuleLogModel = objILogMgrRemote.createModuleLog(objSaveModuleLogModel);

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				objILogMgrHome = null;
				objILogMgrRemote = null;
			}
			//SM/7/14/2010
			
			if (jdtpMain.getAllFrames().length == 0)
			{
					jdtpMain.add(jif);
					internalFramesVect.addElement(jif);
					moduleLogVect.addElement(objModuleLogModel);//SM/7/14/2010
					jif.toFront();
			}
			else
			{
					JInternalFrame arr[] = jdtpMain.getAllFrames();
					jdtpMain.add(jif);
					internalFramesVect.addElement(jif);
					moduleLogVect.addElement(objModuleLogModel);//SM/7/14/2010

			} //end of else

		try
        {
			jif.setSelected(true);
			jif.toFront();
        }
        catch (java.beans.PropertyVetoException exc)
        {
			Debug.print(STR_CLASS_NAME, "java.beans.PropertyVetoException ", "addInternalFrame",exc, true);
        }

    } //end of addtoInternalFrame


    public void addToWindowList()
    {
        jmnuWindow.removeAll();
		for (int i = 0; i < internalFramesVect.size(); i++) {
				jmi = new JCheckBoxMenuItem();
				jmi.setText(((JInternalFrame)internalFramesVect.get(i)).getTitle());
				if(((JInternalFrame)internalFramesVect.get(i)).isSelected())
				{
					jmi.setState(true);
				}
				jmnuWindow.add(jmi);
				jmi.setName(Integer.toString(i));

				hashWindows.put(Integer.toString(i), jmi);

				jmi.addActionListener(new java.awt.event.ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						jmi_actionPerformed(e);
					}
				});

        }
    }

   private void jmi_actionPerformed(ActionEvent ae){
		String strName = ((JCheckBoxMenuItem) ae.getSource()).getName();

		for (int i = 0; i < internalFramesVect.size(); i++){
			unCheckWindowItems();
			JCheckBoxMenuItem jcbmi = (JCheckBoxMenuItem) hashWindows.get(strName);
			jcbmi.setState(true);

			try{
				((JInternalFrame)internalFramesVect.get(Integer.parseInt(strName))).toFront();
				((JInternalFrame)internalFramesVect.get(Integer.parseInt(strName))).setSelected(true);
				((JInternalFrame)internalFramesVect.get(Integer.parseInt(strName))).setIcon(false);
			}
			 catch (java.beans.PropertyVetoException exc){
				 Debug.print(STR_CLASS_NAME, "java.beans.PropertyVetoException ", "jmi_actionPerformed",exc, true);
			}
		}
    }


	public void removeFromWindowList(){
        for (int i = 0; i < internalFramesVect.size(); i++) {
            if(((JInternalFrame)internalFramesVect.get(i)).isClosed())
            {
                internalFramesVect.remove(i);
				ILogMgrHome objILogMgrHome;
				ILogMgrRemote objILogMgrRemote;
				ModuleLogModel objModuleLogModel = (ModuleLogModel)moduleLogVect.remove(i);
				ModuleLogModel objSaveModuleLogModel = createModuleLogModel(new Integer("0"), "EXIT", objModuleLogModel);
				try {
					
					objILogMgrHome = (ILogMgrHome)EJBUtil.lookupHomeInterface(EJBUtil.LOG_MGR_HOME);
					objILogMgrRemote = objILogMgrHome.create();
					objILogMgrRemote.updateModuleLog(objSaveModuleLogModel);

				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					objILogMgrHome = null;
					objILogMgrRemote = null;
				}
				//SM/7/14/2010
				break;
            }
        }
	}

    public void unCheckWindowItems()
    {
        Enumeration e = hashWindows.elements();
        while (e.hasMoreElements())
        {
            JCheckBoxMenuItem cbmi = (JCheckBoxMenuItem) e.nextElement();

            if (cbmi.isSelected())
            {
                cbmi.setState(false);
            }//if()
        }
    }//unCheckWindowItems()

    public Object checkInstance(String STR_CLASS_NAME)
    {
        Object objFrame = null;

        try
        {
            /* CHECK IF THE FRAME ALREADY EXISTS. IF IT DOES THEN OPEN THE SAME FRAME */
            JInternalFrame arr[] = jdtpMain.getAllFrames();

            for (int i = 0; i < arr.length; i++)
            {
                JInternalFrame objJIFrame = (JInternalFrame) arr[i];
                if (objJIFrame.getClass().getName().equals(Class.forName(STR_CLASS_NAME).getName()))
                {
                    objFrame = objJIFrame;
                    break;
                }
            }
        }
        catch (ClassNotFoundException cnfe)
        {
			Debug.print(STR_CLASS_NAME, "ClassNotFoundException ", "checkInstance",cnfe, true);
        }
        return objFrame;
    }

    public void callVendorScan(Integer intVendId, Integer intTabNo)
    {
        try
        {
            JIFraVendorScan objFrame = (JIFraVendorScan) checkInstance("com.hoc.ejems2.client.frames.JIFraVendorScan");
            objFrame.LOAD_ALL_VENDORS = true;
            if (objFrame == null)
            {
                objFrame.LOAD_ALL_VENDORS = false;
                objFrame = new JIFraVendorScan("Vendor Scan", this);
                addInternalFrame(objFrame,getModuleId(ClientUtility.MOD_VENDOR_SCAN));//SM/7/14/2010
            }
            objFrame.setIcon(false);
            objFrame.toFront();
            objFrame.resetVendorScan(intVendId, intTabNo);
        }
        catch (Exception e)
        {
			Debug.print(STR_CLASS_NAME, null, "callVendorScan",e, true);
        }
    }

    public void callCustomerScan(Integer intCustId, Integer intTabNo)
    {
        try
        {
            JIFraCustomerScan objFrame = (JIFraCustomerScan) checkInstance("com.hoc.ejems2.client.frames.JIFraCustomerScan");
            objFrame.LOAD_ALL_CUSTOMERS = true;
            if (objFrame == null)
            {
                objFrame.LOAD_ALL_CUSTOMERS = false;
                objFrame = new JIFraCustomerScan("Customer Scan", this);
                addInternalFrame(objFrame,getModuleId(ClientUtility.MOD_CUSTOMER_SCAN));//SM/7/14/2010
            }
            objFrame.setIcon(false);
            objFrame.toFront();
            objFrame.resetCustomerScan(intCustId, intTabNo);
        }
        catch (Exception e)
        {
			Debug.print(STR_CLASS_NAME, null, "callCustomerScan",e, true);
        }
    }

	/**
		This method is written by Vimal Shah to enable the user to select another company to
		which he wants to work with. For doing this this method makes an object of
		LoginToDifferentCompany that opens up a dialog box to enable user to log in another
		company.
	*/
	public void processLoggingInDifferentCompany() {

		JInternalFrame arrAllIntFrms[] = jdtpMain.getAllFrames();
		if (arrAllIntFrms.length != 0) {

			JOptionPane.showMessageDialog(null, "Close all windows before changing company",
				"eJems2", JOptionPane.ERROR_MESSAGE);
			return;

		} // if (arrAllIntFrms.length != 0)

		/*
			The value selected by the user from company drop down in the opened dialog
			will be saved in this vector.
		*/
		Vector vecSelectedCompany = new Vector();
		IUserLoginMgrHome objUserLoginMgrHome;
		IUserLoginMgrRemote objUserLoginMgrRemote;
		
	
			objBO.setAction("OUT");//TM 4/21/2006
			
		try {

			new JDiaChangeCompany(new JFrame(),
				"eJEMS2 - Change Company", vecSelectedCompany);

			if (vecSelectedCompany.size() > 0) {
				/* Getting login Id from existing BoundObject */
				String strLoginId = objBO.getLoginId();

				/* Getting required values from vector that got values from JDiaChangeCompany */
				String strCompanyCode = vecSelectedCompany.elementAt(1).toString();
				String strCompanyName = vecSelectedCompany.elementAt(2).toString();
				Integer intCompanyId = new Integer(vecSelectedCompany.elementAt(0).toString());
					
				/* Getting Home Interface for UserLoginMgr */
				objUserLoginMgrHome = (IUserLoginMgrHome)
					EJBUtil.lookupHomeInterface(EJBUtil.LOGIN_MGR_HOME);

				/* Getting Remote Interface for UserLoginMgr */
				objUserLoginMgrRemote = objUserLoginMgrHome.create();

				objUserLoginMgrRemote.insertUserLogInfo(objBO);//TM 4/21/2006

				/* Requesting for the new BoundObject from session bean */
				BoundObject objNewBO = objUserLoginMgrRemote.getUserRightsForNewCompany(
					strLoginId, intCompanyId, strCompanyCode, strCompanyName);
				
				/* Resetting old Objects that will be replaced by new one */
				bFlagPricing = false;
				bFlagStyleCostList = false;
				bFlagSalesScan = false;
				bFlagPurchaseScan = false;
				bFlagInvScan = false;

				/* Nullifying the BoundObject */
				objBO = null;

				/* Clearing the image map that may have got some images in it */
				objImageMap.clear();
				strLastImagePath = "";

				/* PSK 11/23/07 : Clearing the style image buffer that may have got some images in it */
				objImageBuffer.clear();
				arrlImageBufferQueue.clear();

				/* Setting the new BoundObject for this class */
				this.objBO = objNewBO;

				/* Getting user rights for the new company logged in */
				this.htMenuRights = objNewBO.getModuleRights();

				/* Invoking a method to add all menus and its respective menu items */
				addOptionsUnderMenus();

				/* Adding ActionListeners to required components */
				addActionListeners();

				//PNS21JULY2003 - added
				setCompanyIcon(objBO.getCompanyCode()); //AST-01/06/23 Set Company Icon 
				/* Set company background image */
				setCompanyBackgroundImage(objBO.getCompanyID());
				getLoggedInCompanyDefaultInformation();
			    this.setTitle("Ejems3X -"+objBO.getCompanyName().toString());//AST-11/02/22

			} else {


			} // if (vecSelectedCompany.size() > 0)

		} catch (NamingException ne) {
			Debug.print(STR_CLASS_NAME, "NamingException ", "processLoggingInDifferentCompany",ne, true);

		} catch (CreateException ce) {
			Debug.print(STR_CLASS_NAME, "CreateException ", "processLoggingInDifferentCompany",ce, true);

		} catch (RemoteException re) {
			Debug.print(STR_CLASS_NAME, "RemoteException ", "processLoggingInDifferentCompany",re, true);
		}

	} // processLoggingInDifferentCompany


    /**
     * This is a common method showing up a message box
     * @param String Message to be displayed
     * @param int the message severity as defined in the class JOptionPane
     * @returns void
     */
    private void showMessageBox (String Message, int iMsgSeverity) {

        JOptionPane.showMessageDialog(null, Message, "Ejems 2", iMsgSeverity);
    }//showMessageBox()


	/**
	 *
	 *	This method calls getCompanyInformation method on EjemsMainMgr and gets
	 *	CompanyModel in return. After getting company model assigns default freight
	 *	and default inventory type to global variables defined here in this class.
	 *	These variable are then referred to in individual classes.
	 *
	 *	@param
	 *	@return		void
	 *	@exception
	 *
     */
    public void getLoggedInCompanyDefaultInformation() {

		try {

			CompanyModel objCompModel = new CompanyModel();
			objCompModel = objIEjemsMainMgrRemote.getCompanyInformation(
				new Integer(objBO.getCompanyID()));

			if (objCompModel != null) {

				DEFAULT_FREIGHT = objCompModel.getDefaultFreightCharge() == null ? 0.00 :
					objCompModel.getDefaultFreightCharge().doubleValue();

				DEFAULT_INV = objCompModel.getDefaultInventoryType() == null ? "JE" :
					objCompModel.getDefaultInventoryType().trim();

				FLAG_SP_COMM_MSG = objCompModel.getFlagSPCommMsg();///RS/07/15/2008

			} else {

				DEFAULT_FREIGHT = 0.00;
				DEFAULT_INV = "JE";

			}

				// if (objCompModel != null)

		} catch(RemoteException re) {

			Debug.print(STR_CLASS_NAME, "RemoteException ", "getLoggedInCompanyDefaultInformation",re, true);
		}

	} // getLoggedInCompanyDefaultInformation

	//NAM08APR03
	/**
	 * This method returns instance of CompanyModel. This method can be accessed from
	 * other modules to get Company model having company related information.
	 */
	public CompanyModel getCompanyModel()
	{
		CompanyModel objCompModel = new CompanyModel();
		try
		{
			objCompModel = objIEjemsMainMgrRemote.getCompanyInformation(
				new Integer(objBO.getCompanyID()));
		}
		catch(RemoteException re)
		{
			Debug.print(STR_CLASS_NAME, "RemoteException ", "getCompanyModel()",re, true);
		}
		return objCompModel;
	}


   //  ADDED BY SIDDHI
   //   SJ29042003

	public static Document createUserConfiguration()
	{
		Element element = null;
        Element elementRoot = null;
        String strLoginId = Ejems2Main.objBO.getLoginId().toLowerCase();;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
		Document docSave;
		docSave = Ejems2Main.docUser;
        try
		{
                db = dbf.newDocumentBuilder();
         }
		catch (ParserConfigurationException e)
		{
			Debug.print(STR_CLASS_NAME, "ParserConfigurationException ", "createUserConfiguration()",e, true);
		}

		  docSave = db.newDocument();
		  elementRoot = docSave.createElement("user-preferences");
          elementRoot.setAttribute("code" , strLoginId); //PNS16DEC2002 - changed from Ejems2Main.objBO.getLoginId() to strLoginId to take care of upper case and lower case
          docSave.appendChild(elementRoot);

		//formating
         elementRoot.appendChild(docSave.createTextNode("\n\t"));

          element = docSave.createElement("default");
          element.setAttribute("company" , ""+Ejems2Main.objBO.getCompanyID());
          element.setAttribute("module" , "");
          elementRoot.appendChild(element);
          //formating
           elementRoot.appendChild(docSave.createTextNode("\n\t"));


			Ejems2Main.docUser = docSave;

			return docSave;



	}

//	SJ29042003
//  ADDED BY SIDDHI
	//NAM09JUL03
	public static Element createUserPreferenceCompanyTag(int companyID)
	{
		element = docUser.createElement("company");
        element.setAttribute("id" , ""+companyID);
        docUser.getDocumentElement().appendChild(element);
        docUser.getDocumentElement().appendChild(docUser.createTextNode("\n\t"));
		return element;
    }

	    //  ADDED BY SIDDHI - SJ29042003

	 //NAM09JUL03
	 public static Element createUserPreferenceModuleTag(Element elementCompany, String strModuleName)
	 {
		element = Ejems2Main.docUser.createElement("module");
		element.setAttribute("code" , strModuleName);
        elementCompany.appendChild(element);
        docUser.getDocumentElement().appendChild(docUser.createTextNode("\n\t"));
		return element;
	 }

	/** TM 1/16/2008	
	  *	This method is used to Show the matching solitaire found to the equest placed by the customer from 
	  *	internal soltaire search page.
	  */ 
	 public void findMatchingSolitaireForRequirement(){
		Calendar cal = new GregorianCalendar();    				
		// 1=Sunday, 2=Monday, 
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK); 
		if(dayOfWeek != 6 && dayOfWeek != 7){
			// 0..23
			int hour24 = cal.get(Calendar.HOUR_OF_DAY);
			if (hour24 > 8 && hour24 < 16){	
			
				Properties prop = new Properties();			
				int period = 0;
				try{
					String strEjemsPropFile =  "com/hoc/ejems2/client/util/Ejems2ClientProperties.properties";
					FileInputStream in = new FileInputStream((ClassLoader.getSystemResource(strEjemsPropFile).getPath()));
					prop.load(in);
					period = Integer.parseInt(prop.getProperty("TimeInterval"));
					in.close();					
				}catch(Exception e){
					Debug.print(STR_CLASS_NAME, "Exception", "Properties file data", e, true);
				}
				int delay = 300000;   // delay for 5 min.
				int period1 = period * 60000;
				Timer timer = new Timer();
				
				timer.scheduleAtFixedRate(new TimerTask() {
						public void run() {
							// Task here ...
						  Calendar cal1 = new GregorianCalendar();						 

						  ISolRequirementMgrHome objISolRequirementMgrHome;
					 	  ISolRequirementMgrRemote objISolRequirementMgrRemote;
						  ArrayList arrRequirements = new ArrayList();
						  try{
								objISolRequirementMgrHome = (ISolRequirementMgrHome) EJBUtil.lookupHomeInterface(EJBUtil.SOLITAIRE_REQUIREMENT_MGR_HOME);
								objISolRequirementMgrRemote = objISolRequirementMgrHome.create();

								arrRequirements = objISolRequirementMgrRemote.getRequirements(new Integer(objBO.getUserID()));
							/* Records are prsent then display in the dialog box */
								if (arrRequirements.size() > 0){
									 if(objJIfraSolReqMatching == null)
									 {	
										objJIfraSolReqMatching = new JIfraSolReqMatching(new Integer(objBO.getUserID()),arrRequirements,ej2Main);//TM 3/20/2008
										addInternalFrame(objJIfraSolReqMatching,getModuleId(ClientUtility.MOD_SOL_RQUIREMENT));//SM/7/14/2010										
								 	 }
									 //If already open and closed
									 else if(objJIfraSolReqMatching !=null && !objJIfraSolReqMatching.isVisible())
									 {
									 	objJIfraSolReqMatching = new JIfraSolReqMatching(new Integer(objBO.getUserID()),arrRequirements,ej2Main);//TM 3/20/2008
										addInternalFrame(objJIfraSolReqMatching,getModuleId(ClientUtility.MOD_SOL_RQUIREMENT));//SM/7/14/2010	
									 }
									 //If screen is still open
									 else if(objJIfraSolReqMatching!=null && objJIfraSolReqMatching.isVisible())
									 {
										objJIfraSolReqMatching.toFront();
									 }	
								}

						  }catch(Exception e){
							  Debug.print(STR_CLASS_NAME, "Exception", "Find matching solitaire requirement", e, true);
						  }//end of catch

						}
					}, delay, period1);
				
			}//end of time check
		}//end of day check
		
	 }//end of findMatchingSolitaireForRequirement

	 //UL:10/7/2008
	 /*
		Method is use to open the Unmark hold solitaire module while 
		logged in or when we change the company
	 */
	 private void openUnmarkHoldSolitaireModule()
	 {
		IUnmarkHoldSolitairesMgrHome objIUnmarkHoldSolitairesMgrHome;
		IUnmarkHoldSolitairesMgrRemote objUnmarkHoldSolitairesMgrRemote;
		ArrayList arrHoldSolitaires = new ArrayList();
		try{
			objIUnmarkHoldSolitairesMgrHome = (IUnmarkHoldSolitairesMgrHome) EJBUtil.lookupHomeInterface(EJBUtil.UNMARK_HOLD_SOLITAIRES);
			objUnmarkHoldSolitairesMgrRemote = objIUnmarkHoldSolitairesMgrHome.create();

			arrHoldSolitaires = objUnmarkHoldSolitairesMgrRemote.getOnHoldSolitaires(new Integer(objBO.getCompanyID()));
		/* Records are prsent then display in the dialog box */
			if (arrHoldSolitaires.size() > 0){
				 if(objHoldSolitaire == null)
				 {	
					objHoldSolitaire = new JIFraUnmarkHoldSolitaires(new Integer(objBO.getUserID()),arrHoldSolitaires,ej2Main);
					addInternalFrame(objHoldSolitaire,getModuleId(ClientUtility.MOD_UNMARK_HOLD_SOL));//SM/7/14/2010								
				 }
				 //If already open and closed
				 else if(objHoldSolitaire !=null && !objHoldSolitaire.isVisible())
				 {
					objHoldSolitaire = new JIFraUnmarkHoldSolitaires(new Integer(objBO.getUserID()),arrHoldSolitaires,ej2Main);
					addInternalFrame(objHoldSolitaire,getModuleId(ClientUtility.MOD_UNMARK_HOLD_SOL));//SM/7/14/2010
				 }
				 //If screen is still open
				 else if(objHoldSolitaire !=null && objHoldSolitaire.isVisible())
				 {
					objHoldSolitaire.toFront();
				 }	
			}

		}catch(Exception e){
		  Debug.print(STR_CLASS_NAME, "Exception", "Find matching solitaire requirement", e, true);
		}//end of catch
	 }

	 public ModuleLogModel createModuleLogModel(Integer intModuleId, String strLogType, ModuleLogModel objModuleLogModel){
		
		ModuleLogModel objModel = new ModuleLogModel();

		IEjemsMainMgrHome objEjemsMgrHome;
		IEjemsMainMgrRemote objEjemsMgrRemote;
		Timestamp tsCurrentTimeStamp = null;
		try {
			
			objEjemsMgrHome = (IEjemsMainMgrHome) EJBUtil.lookupHomeInterface(EJBUtil.EJEMSMAIN_SESSION_HOME);
			objEjemsMgrRemote = objEjemsMgrHome.create();
			tsCurrentTimeStamp = objEjemsMgrRemote.getServerTimestamp();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			objEjemsMgrHome = null;
			objEjemsMgrRemote = null;
		}

		if (strLogType.equalsIgnoreCase("OPEN")){
				
				objModel.setCompanyId(new Integer (objBO.getCompanyID()));
				objModel.setModuleId(intModuleId);
				objModel.setUserId(new Integer (objBO.getUserID()));
				objModel.setDtOpenTime(tsCurrentTimeStamp);
				objModel.setDtExitTime(null);

		}else{
				objModel = objModuleLogModel;
				objModel.setDtExitTime(tsCurrentTimeStamp);	
		}

		return objModel;
	 }//createModuleLogModel

} // Ejems2Main


/****************************************************************************************
 *																						*
 *	File name   : Ejems2Main.java														*
 *																						*
 *																						*
 *	COPYRIGHT NOTICE. Copyright � 2001 House of Code, Inc., 589 Fifth Avenue, Suite 	*
 *	1004, New York, NY 10017 All rights reserved. This software and documentation is	*
 *	subject to and made available only pursuant to the terms of the House of Code		*
 *	License Agreement and may be used or copied only in accordance with the terms of	*
 *	that agreement. It is against the law to copy the software except as specifically	*
 *	allowed in the agreement. This document may not, in whole or in part, be copied,	*
 *	photocopied, reproduced, translated, or reduced to any electronic medium or			*
 *	machine-readable form without prior consent, in writing, from House of Code, Inc. 	*
 *																						*
 *																						*
 ****************************************************************************************/
