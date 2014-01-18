package it.cnr.ilc.ga.action.user;


import java.io.IOException;
import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;

@ManagedBean
@SessionScoped
public class LoginBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8140677690010150074L;

	// FIXME Simple user database 
	private static final String[] users = {"angelo:angelo","fede:fede","ouafae:ouafae"};

	private String username;
	private String password;
	private String role;
	private boolean loggedIn;
	
	
	@ManagedProperty(value="#{navigationBean}")
	private NavigationBean navigationBean;

	/**
	 * Login operation.
	 * @return
	 */
	public String doLogin() {
		// Get every user from our sample database :)
		for (String user: users) {
			String dbUsername = user.split(":")[0];
			String dbPassword = user.split(":")[1];

			// Successful login
			if (dbUsername.equals(username) && dbPassword.equals(password)) {
				loggedIn = true;
				
				addMessageToContext("Login done", "loginMessage", FacesMessage.SEVERITY_INFO);
				
				return navigationBean.redirectToWelcome();
			}
		}
		
		if(!loggedIn){
			addMessageToContext("Login incorrect", "loginMessage", FacesMessage.SEVERITY_WARN);
			System.err.println("non loggato!!");
		}


		return ""; //navigationBean.toLogin();

	}

	/**
	 * Logout operation.
	 * @return
	 */
	public String doLogout() {
		// Set the logged paremeter in to false indicating that user is not logged in anymore 
		loggedIn = false;
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();

		addMessageToContext("Logout done", "loginMessage", FacesMessage.SEVERITY_INFO);
		return navigationBean.toLogin();
	}

	
	private static void addMessageToContext(String summary, String messageId, Severity severity) {
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage();
		message.setSeverity(severity);
		message.setSummary(summary);
		context.addMessage(messageId, message);
	}

	public void verifyUseLogin(ComponentSystemEvent event){
		if(!loggedIn){
			//System.err.println("User is NOT logged in");
			ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
			try {
				ec.redirect(ec.getRequestContextPath() + navigationBean.redirectToLogin());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else { 
			//System.err.println("User is logged in");
		}
	}
	
	// ------------------------------
	// Getters & Setters 

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean getLoggedIn() {
		return loggedIn;
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	/**
	 * @return the role
	 */
	public String getRole() {
		return role;
	}

	/**
	 * @param role the role to set
	 */
	public void setRole(String role) {
		this.role = role;
	}

	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	public void setNavigationBean(NavigationBean navigationBean) {
		this.navigationBean = navigationBean;
	}

	
	
	
}
