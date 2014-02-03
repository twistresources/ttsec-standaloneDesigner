package tooltwist.ttsec_standaloneDesigner;

import com.dinaa.xpc.*;
import com.dinaa.xpc.backend.XpcSecurityImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tooltwist.misc.TtConfig;
import tooltwist.wbd.DesignerRole;

/**
 * Insert the type's description here. Creation date: (13/03/2001 9:50:58 AM)
 * 
 * @author: Administrator
 */
// STD-1  Implement DesignerSecurityPlugin
public class StandaloneDesignerSecurityPlugin extends XpcSecurityImpl// implements DesignerSecurityPluggin,
{
	static Logger logger = LoggerFactory.getLogger(StandaloneDesignerSecurityPlugin.class);
	public static final String USERTYPE_CUSTOMER = "C"; // coCustomerMaster
	public static final String USERTYPE_EMPLOYEE = "E"; // coEmployee
	public static final String USERTYPE_SUPPLIER = "S"; // coSupplierMaster
	public static final String USERTYPE_CONTACT = "T"; // coContactMaster
	public static final String USERTYPE_ORGANIZATION = "O"; // rmOrganization

	public static final String DEFAULT_APPEARANCE = "silver";

	/*
	 * Note from Phil: The same error message must be displayed for invalid username and invalid password. If they have
	 * different error messages, it allows confirmation that a user id exists and assists hacking attempts.
	 */
	static final String INVALID_USERNAME_OR_PASSWORD = "Invalid User Id or Password";
	static final String INVALID_USERNAME_FOR_SSO = "Invalid User Id. Application is SSO enabled and your user id is not registred with PHINZA. Please contact your PHINZA administrator.";

	/**
	 * TestSecurityPluggin constructor comment.
	 * 
	 * @throws XpcException
	 */
	public StandaloneDesignerSecurityPlugin() throws XpcException {
		super();
	}

	@Override
	public boolean login(String userType, String userName, String password, XpcSecurityPlugginParameter details) throws XpcSecurityException, XpcException {
		this.setValue("userName", "Temporary Login");
		this.setValue("fullName", "Temporary User");
		this.setValue("userType", TtConfig.USER_TYPE);

		this.setValue("customerName", "Temporary Login");
		this.setUserPreference("language", "EN");
		this.setUserPreference("lineSpeed", "L");

		// sec.setValue("companyNo", nvl(db_company_no));
		// sec.setValue("rmOrganisationLink", nvl(db_user_link));
		// sec.setValue("orgId", nvl(db_org_id));
		// sec.setValue("orgName", nvl(db_org_name));
		this.setValue("userMenu", "mainMenu");
		
		this.setValue("appearance", "white");
		this.setValue("stylesheetPath", "/ttsvr/stylesheet/white");
		this.setValue("imagePath", "/ttsvr/images/b/mall");
		this.setValue("imageFormat", ".png");
		this.setUserPreference("look", "b");
		this.setUserPreference("look2", "white");


		// Set the admin flag.
		this.setValue("isAdministrator", "Y");

		// Login is okay.
		return true;
	}

	@Override
	public boolean mayAccessEntity(String entityName, String operation) {
		return true;
	}

	/**
	 * See if a user identified may run a specific Dinaa module.
	 * 
	 * @return boolean
	 * @param domain
	 *            Domain of the module.
	 * @param name
	 *            The name of the module.
	 */
	@Override
	public boolean mayAccessModule(String name) {
		return true;
	}

	@Override
	public boolean hasRole(String role) {

if (1==1)return true;
		int level = 1;

		
		// Level 1 - content editor 
		if (
			role.equals(DesignerRole.EDIT_TEXT.getRoleCode())
			|| role.equals("ttDesigner")
		) {
			return (level >= 1);
		}
		
		
			
		// Level 2 - Site Layout
		if (
				role.equals(DesignerRole.IMAGES.getRoleCode())
				|| role.equals(DesignerRole.CHANGE_LAYOUT.getRoleCode())
				|| role.equals(DesignerRole.CHANGE_GRIDS.getRoleCode())
				|| role.equals(DesignerRole.ADD_WIDGETS.getRoleCode())
				|| role.equals(DesignerRole.MOVE_WIDGETS.getRoleCode())
				|| role.equals(DesignerRole.DELETE_WIDGETS.getRoleCode())
				|| role.equals(DesignerRole.NAVPOINTS.getRoleCode())
				
				// These should probably be independent of level
				|| role.equals(DesignerRole.SEO.getRoleCode())
				|| role.equals(DesignerRole.APPROVE.getRoleCode())
		) {
			return (level >= 2);
		}
		
		// Level 3 - Web Designer
		if (
				role.equals(DesignerRole.EDIT_JSP.getRoleCode())
				|| role.equals(DesignerRole.EDIT_HTML.getRoleCode())
				|| role.equals(DesignerRole.EDIT_JAVASCRIPT.getRoleCode())
				|| role.equals(DesignerRole.EDIT_CSS.getRoleCode())
				|| role.equals(DesignerRole.EDIT_SNIPPET.getRoleCode())
				) {
			return (level >= 3);
		}
		

		// Level 4 - Programmer
		if (role.equals(DesignerRole.PROGRAMMER.getRoleCode())) {
			return (level >= 4);
		}


		// Other roles
		// We'll just bundle them all together under the programmer level
		if (
				role.equals(DesignerRole.PACKAGES.getRoleCode())
		
				// Controller related
				|| role.equals(DesignerRole.ACCESS_PAYLOAD.getRoleCode())
				|| role.equals(DesignerRole.ACCESS_GENERATOR_MAINT.getRoleCode())
				|| role.equals(DesignerRole.ACCESS_WEBSERVER_MAINT.getRoleCode())
				|| role.equals(DesignerRole.ACCESS_PAYLOAD_MAINT.getRoleCode())
				|| role.equals(DesignerRole.ACCESS_PAYLOAD_TAB.getRoleCode())
		) {
			return (level >= 4);
		}
		
		// STD-1  Move this to DesignerRole
		if (role.equals("ttDesigner-promote-to-production")) {
			return (level >= 3);
		}
		
		return true;
	}

	// @Override
	// public boolean loadRoles()
	// {
	// // STD-1 Shouldn't this use a method?
	// String userName = (String) this.getValue("userName");
	// String userName2 = this.getUserName();
	//
	// logger.debug(">> loadRoles");
	// ArrayList<String> userRoleMenuItemList = new ArrayList<String>();
	// ArrayList<String> userPrivilegeList = new ArrayList<String>();
	//
	// ArrayList<String> userRoleList = new ArrayList<String>();
	//
	// try
	// {
	//
	// // If Administrator do not attempt to load any roles. Futile
	// // excerise.
	// String isAdministrator = (String) this.getValue("isAdministrator");
	// if (isAdministrator.equals("Y"))
	// {
	// this.setValue(XpcSecurity.VARIABLE_USERS_ROLE_LIST, loadAllRolesForAdmin(this));
	// return true;
	// }
	//
	// Xpc xpc = new Xpc(this);
	// xpc.start("phinza.D.sysUserRoles", "select");
	// xpc.attrib("userCode", userName);
	// XData output = xpc.run();
	//
	// logger.debug("UserRoles DATA: " + output.getXml());
	// // Not necessary for each user to have a role, group role to
	// // suffice.
	// // ... for later.
	// if ("select".equals(output.getRootType()))
	// {
	// // Comes here if the user has roles assigned to them. Ignores
	// // group roles.
	// // Now get all the roles that the user has access to.
	// XNodes userRoleRecords = output.getNodes("/*/sysUserRoles");
	// for (userRoleRecords.first(); userRoleRecords.next();)
	// {
	// userRoleList.add(userRoleRecords.getText("roleCode"));
	//
	// xpc = new Xpc(this);
	// xpc.start("phinza.D.sysRoleMenus", "select");
	// xpc.attrib("roleCode", userRoleRecords.getText("roleCode"));
	// XData outputMenuItems = xpc.run();
	//
	// if ("select".equals(outputMenuItems.getRootType()))
	// {
	// XNodes roleItemRecords = outputMenuItems.getNodes("/*/sysRoleMenus");
	// for (roleItemRecords.first(); roleItemRecords.next();)
	// {
	// String menuItem = roleItemRecords.getText("menuItem");
	// userRoleMenuItemList.add(menuItem);
	// }
	// }
	//
	// xpc = new Xpc(this);
	// xpc.start("phinza.D.sysRolePrivileges", "select");
	// xpc.attrib("roleCode", userRoleRecords.getText("roleCode"));
	// XData outputPrivileges = xpc.run();
	//
	// if ("select".equals(outputPrivileges.getRootType()))
	// {
	// XNodes privRecords = outputPrivileges.getNodes("/*/sysRolePrivileges");
	// for (privRecords.first(); privRecords.next();)
	// {
	// String privilegeName = privRecords.getText("privilegeName");
	// userPrivilegeList.add(privilegeName);
	// }
	// }
	// }
	// }
	//
	// // Now try for group roles.
	// xpc = new Xpc(this);
	// xpc.start("phinza.D.sysGroupMap", "select");
	// xpc.attrib("userCode", userName);
	// XData groupOutput = xpc.run();
	//
	// if ("notfound".equals(groupOutput.getRootType()))
	// {
	//
	// // No User roles, now no groups found for this user. Thus
	// // this user does not have any roles at all.
	// logger.warn("User " + userName + " does not have roles and does not belong to any group.");
	//
	// }
	// else
	// {
	// // Groups found for this user. Now see if we can find Group
	// // roles for each Group.
	// XNodes groupMapRecords = groupOutput.getNodes("/*/sysGroupMap");
	// for (groupMapRecords.first(); groupMapRecords.next();)
	// {
	//
	// xpc = new Xpc(this);
	// xpc.start("phinza.D.sysGroupRoles", "select");
	// xpc.attrib("groupCode", groupMapRecords.getText("groupCode"));
	// XData grpRoleOutput = xpc.run();
	//
	// if ("select".equals(grpRoleOutput.getRootType()))
	// {
	// // Group Roles found. Go on and add the items to the
	// // Array.
	// XNodes groupRoleRecords = grpRoleOutput.getNodes("/*/sysGroupRoles");
	// for (groupRoleRecords.first(); groupRoleRecords.next();)
	// {
	// userRoleList.add(groupRoleRecords.getText("roleCode"));
	//
	// xpc = new Xpc(this);
	// xpc.start("phinza.D.sysRoleMenus", "select");
	// xpc.attrib("roleCode", groupRoleRecords.getText("roleCode"));
	// XData outputMenuItems = xpc.run();
	//
	// if ("select".equals(outputMenuItems.getRootType()))
	// {
	// XNodes roleItemRecords = outputMenuItems.getNodes("/*/sysRoleMenus");
	// for (roleItemRecords.first(); roleItemRecords.next();)
	// {
	// String menuItem = roleItemRecords.getText("menuItem");
	// userRoleMenuItemList.add(menuItem);
	// }
	// }
	//
	// xpc = new Xpc(this);
	// xpc.start("phinza.D.sysRolePrivileges", "select");
	// xpc.attrib("roleCode", groupRoleRecords.getText("roleCode"));
	// XData outputPrivileges = xpc.run();
	//
	// if ("select".equals(outputPrivileges.getRootType()))
	// {
	// XNodes privRecords = outputPrivileges.getNodes("/*/sysRolePrivileges");
	// for (privRecords.first(); privRecords.next();)
	// {
	// String privilegeName = privRecords.getText("privilegeName");
	// userPrivilegeList.add(privilegeName);
	// }
	// }
	// }
	// }
	// }
	// }
	//
	// this.setValue(XpcSecurity.VARIABLE_USERS_MENU_ITEMS_LIST, userRoleMenuItemList);
	// this.setValue(XpcSecurity.VARIABLE_USERS_PRIVILEGES_LIST, userPrivilegeList);
	// this.setValue(XpcSecurity.VARIABLE_USERS_ROLE_LIST, userRoleList);
	// }
	// catch (Exception e)
	// {
	// logger.warn("Exception occured loading roles: " + e);
	// return false;
	// }
	// return true;
	//
	// }
	//
	// /**
	// * Load all roles for the current company
	// * @param req
	// * The Request object
	// * @param res
	// * The response object
	// * @return
	// * list of all roles
	// * @throws ServletException
	// */
	// private static ArrayList<String> loadAllRolesForAdmin(XpcSecurity sec)
	// {
	// // Stored in XpcSecurity all applicable User Roles for the currently logged user (roles will be use in Service
	// Catalog Security)
	// // This method will load all roles since currently logged user is an admin
	// String companyNo = sec.getString("companyNo");
	//
	// try
	// {
	//
	// ArrayList<String> userRoleList = new ArrayList<String>();
	// Xpc xpc = new Xpc(sec);
	// xpc.start("phinza.D.sysRoles", "select");
	// xpc.attrib("companyNo", companyNo);
	// XData output = xpc.run();
	//
	// if ("select".equals(output.getRootType()))
	// {
	// XNodes userRoleRecords = output.getNodes("/*/sysRoles");
	// for (userRoleRecords.first(); userRoleRecords.next();)
	// {
	// userRoleList.add(userRoleRecords.getText("roleCode"));
	// }
	// }
	// else
	// {
	// logger.warn("User Admin for Company No." + companyNo + " does not have roles.");
	// }
	// return userRoleList;
	// }
	// catch (Exception e)
	// {
	// logger.warn("Exception occured loading roles: " + e);
	// return null;
	// }
	// }

}
