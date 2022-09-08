<%--
- form.jsp
-
- Copyright (C) 2012-2022 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="urn:jsptagdir:/WEB-INF/tags"%>

<acme:form>
		<jstl:if test="${command != 'create'}">
			<acme:input-textbox code="chef.pimpam.form.label.code" path="code" readonly="true" />
		</jstl:if>			
		<acme:input-textbox code="chef.pimpam.form.label.title" path="title" />
		<acme:input-moment code="chef.pimpam.form.label.creation-moment" path="instantiationMoment" readonly="true" />
		<acme:input-textbox code="chef.pimpam.form.label.description" path="description" />
		<acme:input-moment code="chef.pimpam.form.label.start-date" path="startDate" />
		<acme:input-moment code="chef.pimpam.form.label.finish-date" path="finishDate" />
		<acme:input-money code="chef.pimpam.form.label.budget" path="budget" />
		<jstl:if test="${command == 'show'}">
			<acme:input-money code="chef.pimpam.form.label.convertedBudget" path="budgetConverted" readonly="true" />
		</jstl:if>
		<acme:input-url code="chef.pimpam.form.label.link" path="link" />
		<acme:input-url code="chef.pimpam.form.label.kitchenware-code" path="kitchenwareCode" />
		<jstl:if test="${command == 'show'}">
			<acme:input-url code="chef.pimpam.form.label.kitchenware" path="kitchenwareName" readonly="true"/>
			<acme:input-select code="chef.pimpam.form.label.kitchenware.wareType" path="wareType" readonly="true" >
				<acme:input-option code="chef.pimpam.form.label.kitchenware.ingredient" value="${IngredientType}" selected="${wareType == 'INGREDIENT'}"/>
				<acme:input-option code="chef.pimpam.form.label.kitchenware.kitchen-utensil" value="${KitchenUtensilType}" selected="${wareType == 'KITCHEN_UTENSIL'}" />
			</acme:input-select>
		</jstl:if>
		
		<jstl:if test="${command != 'create'}">
			<acme:submit code="chef.pimpam.form.submit.update" action="/chef/pimpam/update"/>
			<acme:submit code="chef.pimpam.form.submit.delete" action="/chef/pimpam/delete"/>
		</jstl:if>
		
		<jstl:if test="${command == 'create'}">
		<acme:submit code="chef.pimpam.form.submit.create" action="/chef/pimpam/create"/>
	</jstl:if>
			
</acme:form>