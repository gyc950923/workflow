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

var KISBPM = KISBPM || {};
KISBPM.TOOLBAR = {
    ACTIONS: {

    	newModel: function (services) {
    		/* 生成16位uuid，用于作为modelId; */
    		var uuid='xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
    	        var r = Math.random()*16|0, v = c == 'x' ? r : (r&0x3|0x8);
    	        return v.toString(16);
    	    });
    		/* 清空流程编辑窗口的内容 */
    		var canvas=services.$scope.editor.getCanvas();//获取流程编辑窗口的画布对象
    		var shapes=canvas.getChildShapes(true);//获取画布中的所有对象
    		services.$scope.editor.setSelection(shapes) //选中所有对象
    		KISBPM.TOOLBAR.ACTIONS._getOryxEditPlugin(services.$scope).editDelete(); //执行删除对象操作
    		services.$scope.undoStack=[];//清空撤销操作栈
    		services.$scope.redoStack=[];//清空重复操作栈
    		/* 下列循环为让撤销和重复按钮变为不可编辑 */
    		for (var i = 0; i < services.$scope.items.length; i++) {
                var item = services.$scope.items[i];
                if (item.action === 'KISBPM.TOOLBAR.ACTIONS.undo'||item.action === 'KISBPM.TOOLBAR.ACTIONS.redo') {
                    services.$scope.safeApply(function () {
                        item.enabled = false;
                    });
                }
            }
    	    /* 初始化后台undoMap */
    		var tmpUrl = ACTIVITI.CONFIG.context + "/wf/model/newModel";
    		jQuery.ajax({
    			url:tmpUrl,
				async:true,
				data:{
						oldModelId:_MODEL_ID,
						newModelId:uuid,
						//option:"true"
					},
				dataType:"text",
				success:function(data){		
					_MODEL_ID=uuid;
					console.info("newModel:"+_MODEL_ID);  
				}
			});
    		
    	},
    	
        saveModel: function (services) {
        	if(undefined==_MODEL_ID||null==_MODEL_ID||""==_MODEL_ID){
        		//KISBPM.TOOLBAR.ACTIONS.newModel();
        		var uuid='xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
        	        var r = Math.random()*16|0, v = c == 'x' ? r : (r&0x3|0x8);
        	        return v.toString(16);
        	    });
        		var tmpUrl = ACTIVITI.CONFIG.context + "/wf/model/newModel";
        		jQuery.ajax({
        			url:tmpUrl,
    				async:false,
    				data:{
    						oldModelId:_MODEL_ID,
    						newModelId:uuid,
    						option:"false",
    					},
    				dataType:"text",
    				success:function(data){		
    					_MODEL_ID=uuid;
    					console.info("newModel:"+_MODEL_ID);  
    				}
    			});
        	}
        	var json = services.$scope.editor.getJSON();
            json = JSON.stringify(json);
            JSON_MODEL = json;
               
        	//var toTop=(window.screen.availHeight -200)/2-30;//计算模态窗口距顶部的距离，用于上下居中
    		//var toLeft=(window.screen.availWidth  -250)/2;//计算模态窗口距顶部的距离，用于左右居中
    		var URL = 'editor-app/popups/save-model.jsp?version=' + Date.now();

    		var opt = {
    				title:"保存",
    				width:350,
    				height:120,
    				max:false
    			};
    		jQuery.dlg(URL, opt);
 			//window.open(URL,"保存模型","toolbar=no, location=yes, directories=no, status=no, menubar=no, scrollbars=yes, resizable=no, copyhistory=yes, width=300, height=150, top="+toTop+", left="+toLeft);
        },

        undo: function (services) {

            // Get the last commands
            var lastCommands = services.$scope.undoStack.pop();

            if (lastCommands) {
            	
            	
            	if(lastCommands[0]){
            		var ctypes=new Array();
            		var ids = new Array();
            		var tempCtype;
            		var tmpUrl = ACTIVITI.CONFIG.context + "/undoData";
            		if(lastCommands[0].shapesAsJson){//上一步为删除操作时
            			
            			for(var i=0; i< lastCommands[0].shapesAsJson.length;i++){
            				console.info(i + "_resourceId_撤消删除:" + lastCommands[0].shapesAsJson[i].resourceId);
            				ids[i] = lastCommands[0].shapesAsJson[i].resourceId;
            				ctypes[i]=lastCommands[0].shapesAsJson[i].stencil.id;
            			}
            		
            		}else if(lastCommands[0].shape){//上一步操作单个组件时
            			
            			tmpUrl = ACTIVITI.CONFIG.context + "/undoData";//"/redoData";
            			console.info(i + "_resourceId_撤消非删除单个:" + lastCommands[0].shape.resourceId);
            			ids[0] = lastCommands[0].shape.resourceId;
            			tempCtype=lastCommands[0].shape._stencil._jsonStencil.id;
            			ctypes[0] = tempCtype.substr(tempCtype.indexOf("#") + 1);
            		
            		}else if(lastCommands[0].shapes){//上一步操作多个组件时
            			
            			tmpUrl = ACTIVITI.CONFIG.context + "/undoData";//"/redoData";
            			for(var i=0; i< lastCommands[0].shapes.length;i++){
            				console.info(i + "_resourceId_撤消非删除多个:" + lastCommands[0].shapes[i].resourceId);
            				ids[i] = lastCommands[0].shapes[i].resourceId;
            				tempCtype=lastCommands[0].shapes[i]._stencil._jsonStencil.id;
                			ctypes[i] = tempCtype.substr(tempCtype.indexOf("#") + 1);
            			}
            		}
            		
            		if(ids.length > 0){
            			
            			jQuery.ajax({url:tmpUrl,
            				type:"post",
        					async:false,
        					data:{
        						resourceIds:ids.join(","),
        						ctypes:ctypes.join(","),
        						modelId:_MODEL_ID
        						},
        					dataType:"text",
        					success:function(data){
        						console.info("undoItem:" + data);
        						  
        					}
            			});
            		}
            	}
            	
            	
            	
                // Add the commands to the redo stack
                services.$scope.redoStack.push(lastCommands);

                // Force refresh of selection, might be that the undo command
                // impacts properties in the selected item
                if (services.$rootScope && services.$rootScope.forceSelectionRefresh) 
                {
                	services.$rootScope.forceSelectionRefresh = true;
                }
                
                // Rollback every command
                for (var i = lastCommands.length - 1; i >= 0; --i) {
                    lastCommands[i].rollback();
                }
                
                // Update and refresh the canvas
                services.$scope.editor.handleEvents({
                    type: ORYX.CONFIG.EVENT_UNDO_ROLLBACK,
                    commands: lastCommands
                });
                
                // Update
                services.$scope.editor.getCanvas().update();
                services.$scope.editor.updateSelection();
            }
            
            var toggleUndo = false;
            if (services.$scope.undoStack.length == 0)
            {
            	toggleUndo = true;
            }
            
            var toggleRedo = false;
            if (services.$scope.redoStack.length > 0)
            {
            	toggleRedo = true;
            }

            if (toggleUndo || toggleRedo) {
                for (var i = 0; i < services.$scope.items.length; i++) {
                    var item = services.$scope.items[i];
                    if (toggleUndo && item.action === 'KISBPM.TOOLBAR.ACTIONS.undo') {
                        services.$scope.safeApply(function () {
                            item.enabled = false;
                        });
                    }
                    else if (toggleRedo && item.action === 'KISBPM.TOOLBAR.ACTIONS.redo') {
                        services.$scope.safeApply(function () {
                            item.enabled = true;
                        });
                    }
                }
            }
        },

        redo: function (services) {

            // Get the last commands from the redo stack
            var lastCommands = services.$scope.redoStack.pop();
           // alert("_MODEL_ID:" + _MODEL_ID);
            if (lastCommands) {
            	
            	/*
            	if(lastCommands[0]){
            		if(lastCommands[0].shapesAsJson){
            			
            			var ids = new Array();
            			for(var i=0; i< lastCommands[0].shapesAsJson.length;i++){
            				console.info(i + "_resourceId:" + lastCommands[0].shapesAsJson[i].resourceId);
            				ids[i] = lastCommands[0].shapesAsJson[i].resourceId;
            			}
            			
            			jQuery.ajax({url: ACTIVITI.CONFIG.context + "/redoData",
        					async:false,
        					data:{resourceIds:ids.join(",")},
        					dataType:"text",
        					success:function(data){
        						console.info("redoItem:" + data);
        						  
        					}
            			});
            		}
            	}*/
            	
            	
            	if(lastCommands[0]){
            		var ctypes=new Array();
            		var ids = new Array();
            		var tempCtype;
            		var tmpUrl = ACTIVITI.CONFIG.context + "/redoData";
            		if(lastCommands[0].shapesAsJson){//上一步为删除操作时
            			
            			for(var i=0; i< lastCommands[0].shapesAsJson.length;i++){
            				console.info(i + "_resourceId_重复删除:" + lastCommands[0].shapesAsJson[i].resourceId);
            				ids[i] = lastCommands[0].shapesAsJson[i].resourceId;
            				ctypes[i]=lastCommands[0].shapesAsJson[i].stencil.id;
            			}
            		
            		}else if(lastCommands[0].shape){//上一步操作单个组件时
            			
            			tmpUrl = ACTIVITI.CONFIG.context + "/redoData";//"/undoData";
            			console.info(i + "_resourceId_重复非删除单个:" + lastCommands[0].shape.resourceId);
            			ids[0] = lastCommands[0].shape.resourceId;
            			tempCtype=lastCommands[0].shape._stencil._jsonStencil.id;
            			ctypes[0] = tempCtype.substr(tempCtype.indexOf("#") + 1);
            		
            		}else if(lastCommands[0].shapes){//上一步操作多个组件时
            			
            			tmpUrl = ACTIVITI.CONFIG.context + "/redoData";//"/undoData";
            			for(var i=0; i< lastCommands[0].shapes.length;i++){
            				console.info(i + "_resourceId_重复非删除多个:" + lastCommands[0].shapes[i].resourceId);
            				ids[i] = lastCommands[0].shapes[i].resourceId;
            				tempCtype=lastCommands[0].shapes[i]._stencil._jsonStencil.id;
                			ctypes[i] = tempCtype.substr(tempCtype.indexOf("#") + 1);
            			}
            		}
            		
            		if(ids.length > 0){
            			
            			jQuery.ajax({url:tmpUrl,
            				type:"post",
        					async:false,
        					data:{
        						resourceIds:ids.join(","),
        						ctypes:ctypes.join(","),
        						modelId:_MODEL_ID
        						},
        					dataType:"text",
        					success:function(data){
        						console.info("redoItem:" + data);
        						  
        					}
            			});
            		}
            	}
            	
                // Add this commands to the undo stack
                services.$scope.undoStack.push(lastCommands);
                
                // Force refresh of selection, might be that the redo command
                // impacts properties in the selected item
                if (services.$rootScope && services.$rootScope.forceSelectionRefresh) 
                {
                	services.$rootScope.forceSelectionRefresh = true;
                }

                // Execute those commands
                lastCommands.each(function (command) {
                    command.execute();
                });

                // Update and refresh the canvas
                services.$scope.editor.handleEvents({
                    type: ORYX.CONFIG.EVENT_UNDO_EXECUTE,
                    commands: lastCommands
                });

                // Update
                services.$scope.editor.getCanvas().update();
                services.$scope.editor.updateSelection();
            }

            var toggleUndo = false;
            if (services.$scope.undoStack.length > 0) {
                toggleUndo = true;
            }

            var toggleRedo = false;
            if (services.$scope.redoStack.length == 0) {
                toggleRedo = true;
            }

            if (toggleUndo || toggleRedo) {
                for (var i = 0; i < services.$scope.items.length; i++) {
                    var item = services.$scope.items[i];
                    if (toggleUndo && item.action === 'KISBPM.TOOLBAR.ACTIONS.undo') {
                        services.$scope.safeApply(function () {
                            item.enabled = true;
                        });
                    }
                    else if (toggleRedo && item.action === 'KISBPM.TOOLBAR.ACTIONS.redo') {
                        services.$scope.safeApply(function () {
                            item.enabled = false;
                        });
                    }
                }
            }
        },

        cut: function (services) {
            KISBPM.TOOLBAR.ACTIONS._getOryxEditPlugin(services.$scope).editCut();
            for (var i = 0; i < services.$scope.items.length; i++) {
                var item = services.$scope.items[i];
                if (item.action === 'KISBPM.TOOLBAR.ACTIONS.paste') {
                    services.$scope.safeApply(function () {
                        item.enabled = true;
                    });
                }
            }
        },

        copy: function (services) {
            KISBPM.TOOLBAR.ACTIONS._getOryxEditPlugin(services.$scope).editCopy();
            for (var i = 0; i < services.$scope.items.length; i++) {
                var item = services.$scope.items[i];
                if (item.action === 'KISBPM.TOOLBAR.ACTIONS.paste') {
                    services.$scope.safeApply(function () {
                        item.enabled = true;
                    });
                }
            }
        },

        paste: function (services) {
            KISBPM.TOOLBAR.ACTIONS._getOryxEditPlugin(services.$scope).editPaste();
        },

        deleteItem: function (services) {
        	
        	var shapes = services.$scope.editor.getSelection();
        	//alert("shapes:" + shapes.length);
        	/* */
        	var selectArray = new Array();
        	
        	if(shapes && shapes.length > 0){
        		
        		for(var i = 0; i < shapes.length; i++){
        			
        			selectArray.push(shapes[i]);
        			console.info(i + "_type:" + shapes[i]._stencil._jsonStencil.type);
        			
        			if(shapes[i]._stencil._jsonStencil.type == "node"){
        				
        				if(shapes[i].incoming.length > 0){
        					
        					for(var j = 0; j < shapes[i].incoming.length; j++){
        						selectArray.push(shapes[i].incoming[j]);
        					}
                			
                		}
                		if(shapes[i].outgoing.length > 0){
                			
                			for(var k = 0; k < shapes[i].outgoing.length; k++){
                				selectArray.push(shapes[i].outgoing[k]);
                			}
                		}
        			}
        		
        	
            		
            		
        		}
        	
        		//return;
        		
        	}
        	
        	services.$scope.editor.setSelection(selectArray);
        	
        	var ids = new Array();
        	var ctypes=new Array();
        	var tempCtype;
        	for(var i=0; i < selectArray.length;i++){
        		console.info("resourceId:" + selectArray[i].resourceId);
        		ids[i] = selectArray[i].resourceId;
        		tempCtype=selectArray[i]._stencil._jsonStencil.id;
        		ctypes[i] = tempCtype.substr(tempCtype.indexOf("#") + 1);
        	}
        	
        	jQuery.ajax({url: ACTIVITI.CONFIG.context + "/delNodeProperties",
					async:false,
					data:{
							resourceIds:ids.join(","),
							ctypes:ctypes.join(","),
							modelId:_MODEL_ID
						},
					dataType:"text",
					type:"post",
					success:function(data){
						console.info("deleteItem:" + data);
						
						  
					}
			});
			
        	
      
        	
            KISBPM.TOOLBAR.ACTIONS._getOryxEditPlugin(services.$scope).editDelete();
        },

        addBendPoint: function (services) {

            var dockerPlugin = KISBPM.TOOLBAR.ACTIONS._getOryxDockerPlugin(services.$scope);

            var enableAdd = !dockerPlugin.enabledAdd();
            dockerPlugin.setEnableAdd(enableAdd);
            if (enableAdd)
            {
            	dockerPlugin.setEnableRemove(false);
            	document.body.style.cursor = 'pointer';
            }
            else
            {
            	document.body.style.cursor = 'default';
            }
        },

        removeBendPoint: function (services) {

            var dockerPlugin = KISBPM.TOOLBAR.ACTIONS._getOryxDockerPlugin(services.$scope);

            var enableRemove = !dockerPlugin.enabledRemove();
            dockerPlugin.setEnableRemove(enableRemove);
            if (enableRemove)
            {
            	dockerPlugin.setEnableAdd(false);
            	document.body.style.cursor = 'pointer';
            }
            else
            {
            	document.body.style.cursor = 'default';
            }
        },
        
        //设置全局属性
        setGlobalProperties: function (services) {

        	/*
            var dockerPlugin = KISBPM.TOOLBAR.ACTIONS._getOryxDockerPlugin(services.$scope);

            var enableRemove = !dockerPlugin.enabledRemove();
            dockerPlugin.setEnableRemove(enableRemove);
            if (enableRemove)
            {
            	dockerPlugin.setEnableAdd(false);
            	document.body.style.cursor = 'pointer';
            }
            else
            {
            	document.body.style.cursor = 'default';
            }
            
            */
        	//alert("setGlobalProperties");
      	  //var modelMetaData = services.$scope.editor.getModelMetaData();
    	  console.info("id:" + _MODEL_ID);
    	  // console.info("modelMetaData:" + JSON.stringify(modelMetaData));
    	 
	    	if (_MODEL_ID)
	 		{
	    		var URL = 'editor-app/mypages/global_properties.jsp?modelId=' + _MODEL_ID;
        		var opt = {
        				title:"全局属性",
        				width:500,
        				height:310,
        				max:false,
        				resize:false,
        				focus:false
        			};
        		jQuery.dlg(URL, opt);
	 		}
	    	//document.body.style.cursor = 'pointer';
        },
        

        /**
         * Helper method: fetches the Oryx Edit plugin from the provided scope,
         * if not on the scope, it is created and put on the scope for further use.
         *
         * It's important to reuse the same EditPlugin while the same scope is active,
         * as the clipboard is stored for the whole lifetime of the scope.
         */
        _getOryxEditPlugin: function ($scope) {
            if ($scope.oryxEditPlugin === undefined || $scope.oryxEditPlugin === null) {
                $scope.oryxEditPlugin = new ORYX.Plugins.Edit($scope.editor);
            }
            return $scope.oryxEditPlugin;
        },

        zoomIn: function (services) {
            KISBPM.TOOLBAR.ACTIONS._getOryxViewPlugin(services.$scope).zoom([1.0 + ORYX.CONFIG.ZOOM_OFFSET]);
        },

        zoomOut: function (services) {
            KISBPM.TOOLBAR.ACTIONS._getOryxViewPlugin(services.$scope).zoom([1.0 - ORYX.CONFIG.ZOOM_OFFSET]);
        },
        
        zoomActual: function (services) {
            KISBPM.TOOLBAR.ACTIONS._getOryxViewPlugin(services.$scope).setAFixZoomLevel(1);
        },
        
        zoomFit: function (services) {
        	KISBPM.TOOLBAR.ACTIONS._getOryxViewPlugin(services.$scope).zoomFitToModel();
        },
        
        alignVertical: function (services) {
        	KISBPM.TOOLBAR.ACTIONS._getOryxArrangmentPlugin(services.$scope).alignShapes([ORYX.CONFIG.EDITOR_ALIGN_MIDDLE]);
        },
        
        alignHorizontal: function (services) {
        	KISBPM.TOOLBAR.ACTIONS._getOryxArrangmentPlugin(services.$scope).alignShapes([ORYX.CONFIG.EDITOR_ALIGN_CENTER]);
        },
        
        sameSize: function (services) {
        	KISBPM.TOOLBAR.ACTIONS._getOryxArrangmentPlugin(services.$scope).alignShapes([ORYX.CONFIG.EDITOR_ALIGN_SIZE]);
        },
        
        closeEditor: function(services) {
        	window.location.href = "./";
        },
        
        /**
         * Helper method: fetches the Oryx View plugin from the provided scope,
         * if not on the scope, it is created and put on the scope for further use.
         */
        _getOryxViewPlugin: function ($scope) {
            if ($scope.oryxViewPlugin === undefined || $scope.oryxViewPlugin === null) {
                $scope.oryxViewPlugin = new ORYX.Plugins.View($scope.editor);
            }
            return $scope.oryxViewPlugin;
        },
        
        _getOryxArrangmentPlugin: function ($scope) {
            if ($scope.oryxArrangmentPlugin === undefined || $scope.oryxArrangmentPlugin === null) {
                $scope.oryxArrangmentPlugin = new ORYX.Plugins.Arrangement($scope.editor);
            }
            return $scope.oryxArrangmentPlugin;
        },

        _getOryxDockerPlugin: function ($scope) {
            if ($scope.oryxDockerPlugin === undefined || $scope.oryxDockerPlugin === null) {
                $scope.oryxDockerPlugin = new ORYX.Plugins.AddDocker($scope.editor);
            }
            return $scope.oryxDockerPlugin;
        }
    }
};

/** Custom controller for the save dialog */
var SaveModelCtrl = [ '$rootScope', '$scope', '$http', '$route', '$location',
    function ($rootScope, $scope, $http, $route, $location) {

    var modelMetaData = $scope.editor.getModelMetaData();

    var description = '';
    if (modelMetaData.description) {
    	description = modelMetaData.description;
    }
    
    var saveDialog = { 'name' : modelMetaData.name,
            'description' : description};
    
    $scope.saveDialog = saveDialog;
    
    var json = $scope.editor.getJSON();
    json = JSON.stringify(json);

    var params = {
        modeltype: modelMetaData.model.modelType,
        json_xml: json,
        name: 'model'
    };

    $scope.status = {
        loading: false
    };

    $scope.close = function () {
    	$scope.$hide();
    };

    $scope.saveAndClose = function () {
    	$scope.save(function() {
    		window.location.href = "./";
    	});
    };
    $scope.save = function (successCallback) {

        if (!$scope.saveDialog.name || $scope.saveDialog.name.length == 0) {
            return;
        }

        // Indicator spinner image
        $scope.status = {
        	loading: true
        };
        
        modelMetaData.name = $scope.saveDialog.name;
        modelMetaData.description = $scope.saveDialog.description;

        var json = $scope.editor.getJSON();
        
        json = JSON.stringify(json);
        
        var selection = $scope.editor.getSelection();
        $scope.editor.setSelection([]);
        
        // Get the serialized svg image source
        var svgClone = $scope.editor.getCanvas().getSVGRepresentation(true);
        $scope.editor.setSelection(selection);
        if ($scope.editor.getCanvas().properties["oryx-showstripableelements"] === false) {
            var stripOutArray = jQuery(svgClone).find(".stripable-element");
            for (var i = stripOutArray.length - 1; i >= 0; i--) {
            	stripOutArray[i].remove();
            }
        }

        // Remove all forced stripable elements
        var stripOutArray = jQuery(svgClone).find(".stripable-element-force");
        for (var i = stripOutArray.length - 1; i >= 0; i--) {
            stripOutArray[i].remove();
        }

        // Parse dom to string
        var svgDOM = DataManager.serialize(svgClone);

        var params = {
            json_xml: json,
            svg_xml: svgDOM,
            name: $scope.saveDialog.name,
            description: $scope.saveDialog.description
        };

        // Update
        $http({   
        	//method: 'PUT',
        	method: 'POST',
            data: params,
            ignoreErrors: true,
            headers: {'Accept': 'application/json',
                      'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'},
            transformRequest: function (obj) {
                var str = [];
                var str2 = [];
                for (var p in obj) {
                    str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                    str2.push(p + "=" + obj[p]);
                }
                console.log("str2:" + str2.join("&"));
                return str.join("&");
            },
            url: KISBPM.URL.putModel(_MODEL_ID/*modelMetaData.modelId*/)})

            .success(function (data, status, headers, config) {
                $scope.editor.handleEvents({
                    type: ORYX.CONFIG.EVENT_SAVED
                });
                $scope.modelData.name = $scope.saveDialog.name;
                $scope.modelData.lastUpdated = data.lastUpdated;
                
                $scope.status.loading = false;
                $scope.$hide();

                // Fire event to all who is listening
                var saveEvent = {
                    type: KISBPM.eventBus.EVENT_TYPE_MODEL_SAVED,
                    model: params,
                    modelId: _MODEL_ID/*modelMetaData.modelId*/,
		            eventType: 'update-model'
                };
                KISBPM.eventBus.dispatch(KISBPM.eventBus.EVENT_TYPE_MODEL_SAVED, saveEvent);

                // Reset state
                $scope.error = undefined;
                $scope.status.loading = false;

                // Execute any callback
                if (successCallback) {
                    successCallback();
                }

            })
            .error(function (data, status, headers, config) {
                $scope.error = {};
                console.log('Something went wrong when updating the process model:' + JSON.stringify(data));
                $scope.status.loading = false;
            });
    };

}];