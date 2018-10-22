
package com.viettel.vsaadmin.service;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.viettel.vsaadmin.service package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GetLocationsResponse_QNAME = new QName("http://service.vsaadmin.viettel.com/", "getLocationsResponse");
    private final static QName _ResetPassword_QNAME = new QName("http://service.vsaadmin.viettel.com/", "resetPassword");
    private final static QName _GetManagedRolesResponse_QNAME = new QName("http://service.vsaadmin.viettel.com/", "getManagedRolesResponse");
    private final static QName _ApproveRole_QNAME = new QName("http://service.vsaadmin.viettel.com/", "approveRole");
    private final static QName _GetRolesOfAppResponse_QNAME = new QName("http://service.vsaadmin.viettel.com/", "getRolesOfAppResponse");
    private final static QName _LockUsers_QNAME = new QName("http://service.vsaadmin.viettel.com/", "lockUsers");
    private final static QName _GetUserInfoBySyncDateResponse_QNAME = new QName("http://service.vsaadmin.viettel.com/", "getUserInfoBySyncDateResponse");
    private final static QName _GetManagedRoles_QNAME = new QName("http://service.vsaadmin.viettel.com/", "getManagedRoles");
    private final static QName _ApproveRoleResponse_QNAME = new QName("http://service.vsaadmin.viettel.com/", "approveRoleResponse");
    private final static QName _CreateUsersResponse_QNAME = new QName("http://service.vsaadmin.viettel.com/", "createUsersResponse");
    private final static QName _DeleteUserRole_QNAME = new QName("http://service.vsaadmin.viettel.com/", "deleteUserRole");
    private final static QName _GetDepartmentTreeResponse_QNAME = new QName("http://service.vsaadmin.viettel.com/", "getDepartmentTreeResponse");
    private final static QName _UnlockUsersResponse_QNAME = new QName("http://service.vsaadmin.viettel.com/", "unlockUsersResponse");
    private final static QName _ResetPasswordResponse_QNAME = new QName("http://service.vsaadmin.viettel.com/", "resetPasswordResponse");
    private final static QName _GetUserInfoBySyncDateAppcodeResponse_QNAME = new QName("http://service.vsaadmin.viettel.com/", "getUserInfoBySyncDateAppcodeResponse");
    private final static QName _CreateUsers_QNAME = new QName("http://service.vsaadmin.viettel.com/", "createUsers");
    private final static QName _GetUserHaveRole_QNAME = new QName("http://service.vsaadmin.viettel.com/", "getUserHaveRole");
    private final static QName _GetUserInfoResponse_QNAME = new QName("http://service.vsaadmin.viettel.com/", "getUserInfoResponse");
    private final static QName _UpdateUsersResponse_QNAME = new QName("http://service.vsaadmin.viettel.com/", "updateUsersResponse");
    private final static QName _GetAllUserResponse_QNAME = new QName("http://service.vsaadmin.viettel.com/", "getAllUserResponse");
    private final static QName _GetUserRole_QNAME = new QName("http://service.vsaadmin.viettel.com/", "getUserRole");
    private final static QName _DeleteUserRoleResponse_QNAME = new QName("http://service.vsaadmin.viettel.com/", "deleteUserRoleResponse");
    private final static QName _GetAllUser_QNAME = new QName("http://service.vsaadmin.viettel.com/", "getAllUser");
    private final static QName _GetLocations_QNAME = new QName("http://service.vsaadmin.viettel.com/", "getLocations");
    private final static QName _GetRolesOfApp_QNAME = new QName("http://service.vsaadmin.viettel.com/", "getRolesOfApp");
    private final static QName _GetUserInfoBySyncDateAppcode_QNAME = new QName("http://service.vsaadmin.viettel.com/", "getUserInfoBySyncDateAppcode");
    private final static QName _UpdateUsers_QNAME = new QName("http://service.vsaadmin.viettel.com/", "updateUsers");
    private final static QName _GetUserInfo_QNAME = new QName("http://service.vsaadmin.viettel.com/", "getUserInfo");
    private final static QName _LockUsersResponse_QNAME = new QName("http://service.vsaadmin.viettel.com/", "lockUsersResponse");
    private final static QName _UnlockUsers_QNAME = new QName("http://service.vsaadmin.viettel.com/", "unlockUsers");
    private final static QName _GetDepartmentTree_QNAME = new QName("http://service.vsaadmin.viettel.com/", "getDepartmentTree");
    private final static QName _GetUserHaveRoleResponse_QNAME = new QName("http://service.vsaadmin.viettel.com/", "getUserHaveRoleResponse");
    private final static QName _GetUserRoleResponse_QNAME = new QName("http://service.vsaadmin.viettel.com/", "getUserRoleResponse");
    private final static QName _GetUserInfoBySyncDate_QNAME = new QName("http://service.vsaadmin.viettel.com/", "getUserInfoBySyncDate");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.viettel.vsaadmin.service
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ResetPassword }
     * 
     */
    public ResetPassword createResetPassword() {
        return new ResetPassword();
    }

    /**
     * Create an instance of {@link GetManagedRolesResponse }
     * 
     */
    public GetManagedRolesResponse createGetManagedRolesResponse() {
        return new GetManagedRolesResponse();
    }

    /**
     * Create an instance of {@link ApproveRole }
     * 
     */
    public ApproveRole createApproveRole() {
        return new ApproveRole();
    }

    /**
     * Create an instance of {@link GetRolesOfAppResponse }
     * 
     */
    public GetRolesOfAppResponse createGetRolesOfAppResponse() {
        return new GetRolesOfAppResponse();
    }

    /**
     * Create an instance of {@link GetLocationsResponse }
     * 
     */
    public GetLocationsResponse createGetLocationsResponse() {
        return new GetLocationsResponse();
    }

    /**
     * Create an instance of {@link ApproveRoleResponse }
     * 
     */
    public ApproveRoleResponse createApproveRoleResponse() {
        return new ApproveRoleResponse();
    }

    /**
     * Create an instance of {@link CreateUsersResponse }
     * 
     */
    public CreateUsersResponse createCreateUsersResponse() {
        return new CreateUsersResponse();
    }

    /**
     * Create an instance of {@link GetManagedRoles }
     * 
     */
    public GetManagedRoles createGetManagedRoles() {
        return new GetManagedRoles();
    }

    /**
     * Create an instance of {@link GetDepartmentTreeResponse }
     * 
     */
    public GetDepartmentTreeResponse createGetDepartmentTreeResponse() {
        return new GetDepartmentTreeResponse();
    }

    /**
     * Create an instance of {@link DeleteUserRole }
     * 
     */
    public DeleteUserRole createDeleteUserRole() {
        return new DeleteUserRole();
    }

    /**
     * Create an instance of {@link UnlockUsersResponse }
     * 
     */
    public UnlockUsersResponse createUnlockUsersResponse() {
        return new UnlockUsersResponse();
    }

    /**
     * Create an instance of {@link ResetPasswordResponse }
     * 
     */
    public ResetPasswordResponse createResetPasswordResponse() {
        return new ResetPasswordResponse();
    }

    /**
     * Create an instance of {@link LockUsers }
     * 
     */
    public LockUsers createLockUsers() {
        return new LockUsers();
    }

    /**
     * Create an instance of {@link GetUserInfoBySyncDateResponse }
     * 
     */
    public GetUserInfoBySyncDateResponse createGetUserInfoBySyncDateResponse() {
        return new GetUserInfoBySyncDateResponse();
    }

    /**
     * Create an instance of {@link GetUserRole }
     * 
     */
    public GetUserRole createGetUserRole() {
        return new GetUserRole();
    }

    /**
     * Create an instance of {@link DeleteUserRoleResponse }
     * 
     */
    public DeleteUserRoleResponse createDeleteUserRoleResponse() {
        return new DeleteUserRoleResponse();
    }

    /**
     * Create an instance of {@link GetUserInfoBySyncDateAppcodeResponse }
     * 
     */
    public GetUserInfoBySyncDateAppcodeResponse createGetUserInfoBySyncDateAppcodeResponse() {
        return new GetUserInfoBySyncDateAppcodeResponse();
    }

    /**
     * Create an instance of {@link GetUserHaveRole }
     * 
     */
    public GetUserHaveRole createGetUserHaveRole() {
        return new GetUserHaveRole();
    }

    /**
     * Create an instance of {@link GetUserInfoResponse }
     * 
     */
    public GetUserInfoResponse createGetUserInfoResponse() {
        return new GetUserInfoResponse();
    }

    /**
     * Create an instance of {@link UpdateUsersResponse }
     * 
     */
    public UpdateUsersResponse createUpdateUsersResponse() {
        return new UpdateUsersResponse();
    }

    /**
     * Create an instance of {@link CreateUsers }
     * 
     */
    public CreateUsers createCreateUsers() {
        return new CreateUsers();
    }

    /**
     * Create an instance of {@link GetAllUserResponse }
     * 
     */
    public GetAllUserResponse createGetAllUserResponse() {
        return new GetAllUserResponse();
    }

    /**
     * Create an instance of {@link GetDepartmentTree }
     * 
     */
    public GetDepartmentTree createGetDepartmentTree() {
        return new GetDepartmentTree();
    }

    /**
     * Create an instance of {@link GetUserRoleResponse }
     * 
     */
    public GetUserRoleResponse createGetUserRoleResponse() {
        return new GetUserRoleResponse();
    }

    /**
     * Create an instance of {@link GetUserHaveRoleResponse }
     * 
     */
    public GetUserHaveRoleResponse createGetUserHaveRoleResponse() {
        return new GetUserHaveRoleResponse();
    }

    /**
     * Create an instance of {@link GetUserInfoBySyncDate }
     * 
     */
    public GetUserInfoBySyncDate createGetUserInfoBySyncDate() {
        return new GetUserInfoBySyncDate();
    }

    /**
     * Create an instance of {@link GetRolesOfApp }
     * 
     */
    public GetRolesOfApp createGetRolesOfApp() {
        return new GetRolesOfApp();
    }

    /**
     * Create an instance of {@link GetAllUser }
     * 
     */
    public GetAllUser createGetAllUser() {
        return new GetAllUser();
    }

    /**
     * Create an instance of {@link GetLocations }
     * 
     */
    public GetLocations createGetLocations() {
        return new GetLocations();
    }

    /**
     * Create an instance of {@link GetUserInfoBySyncDateAppcode }
     * 
     */
    public GetUserInfoBySyncDateAppcode createGetUserInfoBySyncDateAppcode() {
        return new GetUserInfoBySyncDateAppcode();
    }

    /**
     * Create an instance of {@link UpdateUsers }
     * 
     */
    public UpdateUsers createUpdateUsers() {
        return new UpdateUsers();
    }

    /**
     * Create an instance of {@link GetUserInfo }
     * 
     */
    public GetUserInfo createGetUserInfo() {
        return new GetUserInfo();
    }

    /**
     * Create an instance of {@link LockUsersResponse }
     * 
     */
    public LockUsersResponse createLockUsersResponse() {
        return new LockUsersResponse();
    }

    /**
     * Create an instance of {@link UnlockUsers }
     * 
     */
    public UnlockUsers createUnlockUsers() {
        return new UnlockUsers();
    }

    /**
     * Create an instance of {@link UserInfo }
     * 
     */
    public UserInfo createUserInfo() {
        return new UserInfo();
    }

    /**
     * Create an instance of {@link Objects }
     * 
     */
    public Objects createObjects() {
        return new Objects();
    }

    /**
     * Create an instance of {@link ErrorCode }
     * 
     */
    public ErrorCode createErrorCode() {
        return new ErrorCode();
    }

    /**
     * Create an instance of {@link LogEventLogin }
     * 
     */
    public LogEventLogin createLogEventLogin() {
        return new LogEventLogin();
    }

    /**
     * Create an instance of {@link DepartmentBO }
     * 
     */
    public DepartmentBO createDepartmentBO() {
        return new DepartmentBO();
    }

    /**
     * Create an instance of {@link ObjectRoles }
     * 
     */
    public ObjectRoles createObjectRoles() {
        return new ObjectRoles();
    }

    /**
     * Create an instance of {@link LocationBO }
     * 
     */
    public LocationBO createLocationBO() {
        return new LocationBO();
    }

    /**
     * Create an instance of {@link Users }
     * 
     */
    public Users createUsers() {
        return new Users();
    }

    /**
     * Create an instance of {@link UserRoles }
     * 
     */
    public UserRoles createUserRoles() {
        return new UserRoles();
    }

    /**
     * Create an instance of {@link Role }
     * 
     */
    public Role createRole() {
        return new Role();
    }

    /**
     * Create an instance of {@link Actor }
     * 
     */
    public Actor createActor() {
        return new Actor();
    }

    /**
     * Create an instance of {@link Response }
     * 
     */
    public Response createResponse() {
        return new Response();
    }

    /**
     * Create an instance of {@link UserRole }
     * 
     */
    public UserRole createUserRole() {
        return new UserRole();
    }

    /**
     * Create an instance of {@link User }
     * 
     */
    public User createUser() {
        return new User();
    }

    /**
     * Create an instance of {@link Applications }
     * 
     */
    public Applications createApplications() {
        return new Applications();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetLocationsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.vsaadmin.viettel.com/", name = "getLocationsResponse")
    public JAXBElement<GetLocationsResponse> createGetLocationsResponse(GetLocationsResponse value) {
        return new JAXBElement<GetLocationsResponse>(_GetLocationsResponse_QNAME, GetLocationsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ResetPassword }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.vsaadmin.viettel.com/", name = "resetPassword")
    public JAXBElement<ResetPassword> createResetPassword(ResetPassword value) {
        return new JAXBElement<ResetPassword>(_ResetPassword_QNAME, ResetPassword.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetManagedRolesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.vsaadmin.viettel.com/", name = "getManagedRolesResponse")
    public JAXBElement<GetManagedRolesResponse> createGetManagedRolesResponse(GetManagedRolesResponse value) {
        return new JAXBElement<GetManagedRolesResponse>(_GetManagedRolesResponse_QNAME, GetManagedRolesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ApproveRole }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.vsaadmin.viettel.com/", name = "approveRole")
    public JAXBElement<ApproveRole> createApproveRole(ApproveRole value) {
        return new JAXBElement<ApproveRole>(_ApproveRole_QNAME, ApproveRole.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetRolesOfAppResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.vsaadmin.viettel.com/", name = "getRolesOfAppResponse")
    public JAXBElement<GetRolesOfAppResponse> createGetRolesOfAppResponse(GetRolesOfAppResponse value) {
        return new JAXBElement<GetRolesOfAppResponse>(_GetRolesOfAppResponse_QNAME, GetRolesOfAppResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LockUsers }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.vsaadmin.viettel.com/", name = "lockUsers")
    public JAXBElement<LockUsers> createLockUsers(LockUsers value) {
        return new JAXBElement<LockUsers>(_LockUsers_QNAME, LockUsers.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetUserInfoBySyncDateResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.vsaadmin.viettel.com/", name = "getUserInfoBySyncDateResponse")
    public JAXBElement<GetUserInfoBySyncDateResponse> createGetUserInfoBySyncDateResponse(GetUserInfoBySyncDateResponse value) {
        return new JAXBElement<GetUserInfoBySyncDateResponse>(_GetUserInfoBySyncDateResponse_QNAME, GetUserInfoBySyncDateResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetManagedRoles }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.vsaadmin.viettel.com/", name = "getManagedRoles")
    public JAXBElement<GetManagedRoles> createGetManagedRoles(GetManagedRoles value) {
        return new JAXBElement<GetManagedRoles>(_GetManagedRoles_QNAME, GetManagedRoles.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ApproveRoleResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.vsaadmin.viettel.com/", name = "approveRoleResponse")
    public JAXBElement<ApproveRoleResponse> createApproveRoleResponse(ApproveRoleResponse value) {
        return new JAXBElement<ApproveRoleResponse>(_ApproveRoleResponse_QNAME, ApproveRoleResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateUsersResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.vsaadmin.viettel.com/", name = "createUsersResponse")
    public JAXBElement<CreateUsersResponse> createCreateUsersResponse(CreateUsersResponse value) {
        return new JAXBElement<CreateUsersResponse>(_CreateUsersResponse_QNAME, CreateUsersResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteUserRole }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.vsaadmin.viettel.com/", name = "deleteUserRole")
    public JAXBElement<DeleteUserRole> createDeleteUserRole(DeleteUserRole value) {
        return new JAXBElement<DeleteUserRole>(_DeleteUserRole_QNAME, DeleteUserRole.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetDepartmentTreeResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.vsaadmin.viettel.com/", name = "getDepartmentTreeResponse")
    public JAXBElement<GetDepartmentTreeResponse> createGetDepartmentTreeResponse(GetDepartmentTreeResponse value) {
        return new JAXBElement<GetDepartmentTreeResponse>(_GetDepartmentTreeResponse_QNAME, GetDepartmentTreeResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UnlockUsersResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.vsaadmin.viettel.com/", name = "unlockUsersResponse")
    public JAXBElement<UnlockUsersResponse> createUnlockUsersResponse(UnlockUsersResponse value) {
        return new JAXBElement<UnlockUsersResponse>(_UnlockUsersResponse_QNAME, UnlockUsersResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ResetPasswordResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.vsaadmin.viettel.com/", name = "resetPasswordResponse")
    public JAXBElement<ResetPasswordResponse> createResetPasswordResponse(ResetPasswordResponse value) {
        return new JAXBElement<ResetPasswordResponse>(_ResetPasswordResponse_QNAME, ResetPasswordResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetUserInfoBySyncDateAppcodeResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.vsaadmin.viettel.com/", name = "getUserInfoBySyncDateAppcodeResponse")
    public JAXBElement<GetUserInfoBySyncDateAppcodeResponse> createGetUserInfoBySyncDateAppcodeResponse(GetUserInfoBySyncDateAppcodeResponse value) {
        return new JAXBElement<GetUserInfoBySyncDateAppcodeResponse>(_GetUserInfoBySyncDateAppcodeResponse_QNAME, GetUserInfoBySyncDateAppcodeResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateUsers }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.vsaadmin.viettel.com/", name = "createUsers")
    public JAXBElement<CreateUsers> createCreateUsers(CreateUsers value) {
        return new JAXBElement<CreateUsers>(_CreateUsers_QNAME, CreateUsers.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetUserHaveRole }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.vsaadmin.viettel.com/", name = "getUserHaveRole")
    public JAXBElement<GetUserHaveRole> createGetUserHaveRole(GetUserHaveRole value) {
        return new JAXBElement<GetUserHaveRole>(_GetUserHaveRole_QNAME, GetUserHaveRole.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetUserInfoResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.vsaadmin.viettel.com/", name = "getUserInfoResponse")
    public JAXBElement<GetUserInfoResponse> createGetUserInfoResponse(GetUserInfoResponse value) {
        return new JAXBElement<GetUserInfoResponse>(_GetUserInfoResponse_QNAME, GetUserInfoResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateUsersResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.vsaadmin.viettel.com/", name = "updateUsersResponse")
    public JAXBElement<UpdateUsersResponse> createUpdateUsersResponse(UpdateUsersResponse value) {
        return new JAXBElement<UpdateUsersResponse>(_UpdateUsersResponse_QNAME, UpdateUsersResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAllUserResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.vsaadmin.viettel.com/", name = "getAllUserResponse")
    public JAXBElement<GetAllUserResponse> createGetAllUserResponse(GetAllUserResponse value) {
        return new JAXBElement<GetAllUserResponse>(_GetAllUserResponse_QNAME, GetAllUserResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetUserRole }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.vsaadmin.viettel.com/", name = "getUserRole")
    public JAXBElement<GetUserRole> createGetUserRole(GetUserRole value) {
        return new JAXBElement<GetUserRole>(_GetUserRole_QNAME, GetUserRole.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteUserRoleResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.vsaadmin.viettel.com/", name = "deleteUserRoleResponse")
    public JAXBElement<DeleteUserRoleResponse> createDeleteUserRoleResponse(DeleteUserRoleResponse value) {
        return new JAXBElement<DeleteUserRoleResponse>(_DeleteUserRoleResponse_QNAME, DeleteUserRoleResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAllUser }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.vsaadmin.viettel.com/", name = "getAllUser")
    public JAXBElement<GetAllUser> createGetAllUser(GetAllUser value) {
        return new JAXBElement<GetAllUser>(_GetAllUser_QNAME, GetAllUser.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetLocations }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.vsaadmin.viettel.com/", name = "getLocations")
    public JAXBElement<GetLocations> createGetLocations(GetLocations value) {
        return new JAXBElement<GetLocations>(_GetLocations_QNAME, GetLocations.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetRolesOfApp }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.vsaadmin.viettel.com/", name = "getRolesOfApp")
    public JAXBElement<GetRolesOfApp> createGetRolesOfApp(GetRolesOfApp value) {
        return new JAXBElement<GetRolesOfApp>(_GetRolesOfApp_QNAME, GetRolesOfApp.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetUserInfoBySyncDateAppcode }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.vsaadmin.viettel.com/", name = "getUserInfoBySyncDateAppcode")
    public JAXBElement<GetUserInfoBySyncDateAppcode> createGetUserInfoBySyncDateAppcode(GetUserInfoBySyncDateAppcode value) {
        return new JAXBElement<GetUserInfoBySyncDateAppcode>(_GetUserInfoBySyncDateAppcode_QNAME, GetUserInfoBySyncDateAppcode.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateUsers }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.vsaadmin.viettel.com/", name = "updateUsers")
    public JAXBElement<UpdateUsers> createUpdateUsers(UpdateUsers value) {
        return new JAXBElement<UpdateUsers>(_UpdateUsers_QNAME, UpdateUsers.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetUserInfo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.vsaadmin.viettel.com/", name = "getUserInfo")
    public JAXBElement<GetUserInfo> createGetUserInfo(GetUserInfo value) {
        return new JAXBElement<GetUserInfo>(_GetUserInfo_QNAME, GetUserInfo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LockUsersResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.vsaadmin.viettel.com/", name = "lockUsersResponse")
    public JAXBElement<LockUsersResponse> createLockUsersResponse(LockUsersResponse value) {
        return new JAXBElement<LockUsersResponse>(_LockUsersResponse_QNAME, LockUsersResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UnlockUsers }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.vsaadmin.viettel.com/", name = "unlockUsers")
    public JAXBElement<UnlockUsers> createUnlockUsers(UnlockUsers value) {
        return new JAXBElement<UnlockUsers>(_UnlockUsers_QNAME, UnlockUsers.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetDepartmentTree }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.vsaadmin.viettel.com/", name = "getDepartmentTree")
    public JAXBElement<GetDepartmentTree> createGetDepartmentTree(GetDepartmentTree value) {
        return new JAXBElement<GetDepartmentTree>(_GetDepartmentTree_QNAME, GetDepartmentTree.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetUserHaveRoleResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.vsaadmin.viettel.com/", name = "getUserHaveRoleResponse")
    public JAXBElement<GetUserHaveRoleResponse> createGetUserHaveRoleResponse(GetUserHaveRoleResponse value) {
        return new JAXBElement<GetUserHaveRoleResponse>(_GetUserHaveRoleResponse_QNAME, GetUserHaveRoleResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetUserRoleResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.vsaadmin.viettel.com/", name = "getUserRoleResponse")
    public JAXBElement<GetUserRoleResponse> createGetUserRoleResponse(GetUserRoleResponse value) {
        return new JAXBElement<GetUserRoleResponse>(_GetUserRoleResponse_QNAME, GetUserRoleResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetUserInfoBySyncDate }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.vsaadmin.viettel.com/", name = "getUserInfoBySyncDate")
    public JAXBElement<GetUserInfoBySyncDate> createGetUserInfoBySyncDate(GetUserInfoBySyncDate value) {
        return new JAXBElement<GetUserInfoBySyncDate>(_GetUserInfoBySyncDate_QNAME, GetUserInfoBySyncDate.class, null, value);
    }

}
