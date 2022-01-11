<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<jsp:include page="fragments/headTag.jsp"/>

<body>
<script src="resources/js/common.js" defer></script>
<script src="resources/js/profile.js" defer></script>
<jsp:include page="fragments/bodyHeader.jsp"/>

<script type="text/javascript">
    var i18n = [];
    i18n['common.saved'] = '<spring:message code="common.saved"/>';
</script>

<div class="jumbotron pt-4">
    <div class="container">
        <div class="row">
            <div class="col-5 offset-3">
                <h3><spring:message code="${register ? 'app.register' : 'app.profile'}"/></h3>
                <form class="form-group" id="profileForm" charset="utf-8" accept-charset="UTF-8">

                    <input type="hidden" name="register" id="register" value="${register}">
                    <input type="hidden" class="form-control" name="id" id="id">

                    <div class="form-group">
                        <label for="name" class="col-form-label"><spring:message code="user.name"/></label>
                        <input type="text" class="form-control" id="name" name="name"
                               placeholder="<spring:message code="user.name"/>">
                    </div>

                    <div class="form-group">
                        <label for="email" class="col-form-label"><spring:message code="user.email"/></label>
                        <input type="email" class="form-control" id="email" name="email"
                               placeholder="<spring:message code="user.email"/>">
                    </div>

                    <div class="form-group">
                        <label for="password" class="col-form-label"><spring:message code="user.password"/></label>
                        <input type="password" class="form-control" id="password" name="password"
                               placeholder="<spring:message code="user.password"/>">
                    </div>

                </form>

                <div class="text-right">
                    <a class="btn btn-secondary" href="#" onclick="window.history.back()">
                        <span class="fa fa-close"></span>
                        <spring:message code="common.cancel"/>
                    </a>
                    <button type="button" class="btn btn-primary" onclick="save()">
                        <span class="fa fa-check"></span>
                        <spring:message code="common.save"/>
                    </button>
                </div>

            </div>
        </div>

    <br/>

    <div class="container" id="votingHistoryForm">

        <h3 class="text-center"><spring:message code="profile.votingHistory"/></h3>
        <div class="card border-dark">
            <div class="card-body pb-0">
                <form id="filter">
                    <div class="row">
                        <div class="col-2">
                            <label for="start_date"><spring:message code="profile.filter.StartDate"/></label>
                            <input type="date" name="start_date" id="start_date" autocomplete="off">
                        </div>
                        <div class="col-2">
                            <label for="end_date"><spring:message code="profile.filter.EndDate"/></label>
                            <input type="date" name="end_date" id="end_date" autocomplete="off">
                        </div>
                    </div>
                </form>

                <div class="card-footer text-right">
                    <button class="btn btn-danger" onclick="clearFilter()">
                        <span class="fa fa-remove"></span>
                        <spring:message code="common.cancel"/>
                    </button>
                    <button class="btn btn-primary" onclick="ctx.updateTable()">
                        <span class="fa fa-filter"></span>
                        <spring:message code="profile.filter"/>
                    </button>
                </div>

            </div>


            <br/>
            <table class="table table-striped" id="datatable">
                <thead>
                <tr>
                    <th><spring:message code="profile.table.date"/></th>
                    <th><spring:message code="profile.table.restaurant"/></th>
                </tr>
                </thead>
            </table>
        </div>

    </div>

</div>
</div>
</body>
</html>