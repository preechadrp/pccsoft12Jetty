// redirect to initial page if not logged in
if  (sessionScope.get("user") == null) {
	Executions.sendRedirect("/login");
}