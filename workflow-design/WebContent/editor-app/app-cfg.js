/*
 * Activiti Modeler component part of the Activiti project
 * Copyright 2005-2014 Alfresco Software, Ltd. All rights reserved.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
'use strict';

var ACTIVITI = ACTIVITI || {};

ACTIVITI.CONFIG = {
	//'contextRoot' : '/activiti-explorer/service',
	//'contextRoot' : '/workflow-design/service',
	//'context':'/workflow-design'
	'contextRoot' : getContextPath()+'/service',
	'context':getContextPath()
};

/**
 * 获得项目根路径，等价于jsp页面中
 *  <%
        String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    %>
 * 使用方法：getBasePath();
 * @returns 项目的根路径
 *  
 */
function getBasePath() {
    var curWwwPath = window.document.location.href;
    var pathName = window.document.location.pathname;
    var pos = curWwwPath.indexOf(pathName);
    var localhostPath = curWwwPath.substring(0, pos);
    var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
    return (localhostPath + projectName);
}

/**
 * 获取Web应用的contextPath，等价于jsp页面中
 *  <%
        String path = request.getContextPath();
    %>
 * 使用方法:getContextPath();
 * @returns /项目名称(/EasyUIStudy_20141104)
 */
function getContextPath() {
    return window.document.location.pathname.substring(0, window.document.location.pathname.indexOf('\/', 1));
};