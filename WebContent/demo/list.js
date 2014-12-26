Ext.onReady(function() {
			// 加载快速菜单
			Ext.QuickTips.init();
			var actionUrl = "demo";

			// 加载分页数据
			var store = new Ext.data.Store({
						proxy : new Ext.data.HttpProxy({
									url : actionUrl+'!list'
								}),
						reader : new Ext.data.JsonReader({
									root : 'list',
									totalProperty : 'total',
									id : 'id',
									fields : ['id','code', 'description']
								}),
						remoteSort : true
					});
			// 设置默认的排序字段和升降序
			store.setDefaultSort('code', 'asc');
	
			// 设置复选框
			var sm = new Ext.grid.CheckboxSelectionModel();
			// 设置列
			var cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer(),sm, 
					{header : "编码", width: 120,dataIndex : 'code',sortable : true}, 
					{header : "名称", width: 280,dataIndex : 'description',sortable : true}
			]);
			// 默认排序设置为可排序
			cm.defaultSortable = true;
			// 创建GridPanel
			grid = new Ext.grid.GridPanel({
						el : 'listWin',
						width : 900,
						height : 650,
						title : '演示',
						store : store,
						cm : cm,
						sm : sm,
						animCollapse : false,
						trackMouseOver : false,
						loadMask : {
							msg : '载入中,请稍候...'
						},
						// 上方工具条
						tbar : [{
									id : 'addDicButton',
									text : '增加',
									tooltip : '添加一条记录'
								}, '-', {
									id : 'updateDicButton',
									text : '修改',
									tooltip : '修改所选择的一条记录'
								}, '-', {
									id : 'deleteDicButton',
									text : '删除',
									tooltip : '删除所选择的记录'
								}],
						bbar : new Ext.PagingToolbar({
									pageSize : 20,
									store : store,
									displayInfo : true,
									displayMsg : '显示 {0} - {1} 共 {2} 条',
									emptyMsg : "没有数据显示！",
									beforePageText : "页码 ",
									afterPageText : "共 {0} 页",
									firstText : "首页",
									lastText : "末页",
									nextText : "下一页",
									prevText : "上一页",
									refreshText : "刷新"
								})
					});
			// 加载grid
			grid.render();
			// 设置分页数据
			store.load({
				params : {
						start : 0,
						limit : 20
					}
			});
			
			// 添加
			var addDicWin;
			var addButton = Ext.get('addDicButton');
			addButton.on('click', addButtonClick);
			function addButtonClick() {
				if (!addDicWin) {
					addDicWin = new Ext.Window({
								el : 'addDicWin',
								title : '演示',
								layout : 'fit',
								width : 450,
								height : 180,
								closeAction : 'hide',
								plain : true,
								modal : true,
								items : addForm
							});
				}
				addForm.getForm().reset();
				addDicWin.show(this);
			}

			// 添加form
			var addForm = new Ext.FormPanel({
						frame : true,
						labelAlign : 'right',
						waitMsgTarget : true,
						autoScroll : true,
						buttonAlign : 'center',
						method : 'POST',
						url : actionUrl + '!save',
						items :[{	
									xtype: 'textfield',
									fieldLabel: '编码',
									name: 'code',
									allowBlank : false,
									style : 'ime-mode:disabled',
									maxLength : 20,
									width : 50
								},{	
									xtype: 'textfield',
									fieldLabel: '描述',
									name: 'description',
									allowBlank : true,
									maxLength : 100,
									width : 250
								},{	
									xtype : 'hidden',
									name : 'id'
								}]
			});
			// 验证数据并提交
			addForm.addButton('提交', function() {
					if(addForm.form.isValid()){
						addForm.getForm().submit({
									waitMsg : '保存数据...',
									success : function() {
										store.reload();
										Ext.MessageBox.alert('提示', '提交成功！');
										addDicWin.hide();
									},
									failure : function() {
										Ext.MessageBox.alert('提示', '提交失败！');
										addDicWin.hide();
									}
								});
					}
			});

			addForm.addButton('关闭', function() {
					addDicWin.hide();
			});

			// 删除
			var deleteButton = Ext.get('deleteDicButton');
			deleteButton.on('click', function() {
						if (grid.getSelectionModel().getSelections().length > 0)
							Ext.MessageBox.confirm('消息', '确认要删除所选记录?', doDelProc);
						else
							Ext.MessageBox.alert('警告', '最少需要选择一条记录!');
			});

			function doDelProc(btn) {
				if (btn == 'yes') {
					if (grid.getSelectionModel().hasSelection()) {
						var ids = '';
						var records = grid.getSelectionModel().getSelections();
						for (var i = 0; i < records.length; i++) {
							if (i == 0) {
								ids = records[0].data["id"];
							} else {
								ids += ',' + records[i].data["id"];
							}
						}
		
						Ext.Ajax.request({
									method : 'POST',
									url : actionUrl + '!delete',
									params: {ids: ids}, 
									success : function() {
										Ext.MessageBox.alert('提示', '数据删除成功！');
										store.reload();
									},
									failure : function() {
										Ext.MessageBox.alert('提示', '数据删除失败！');
										store.reload();
									}
								});
					}
				}
			};

			// 修改
			var updateButton = Ext.get('updateDicButton');
			updateButton.on('click', function() {
				var records = grid.getSelectionModel().getSelections();
				if (records.length > 1) {
					Ext.MessageBox.alert('提示', '一次不能修改多条记录!');
					return;
				} else if (records.length == 0) {
					Ext.MessageBox.alert('提示', '选择需要修改的一条记录!');
					return;
				} else {
					Ext.Ajax.request({
						method : 'POST',
						url : actionUrl + '!get',
						params: {id: records[0].id},
						success : function(response, options) {
							var obj = Ext.decode(response.responseText);
							if(Ext.isIE){
								document.getElementById("addDicButton").click();
							}else{
								addButtonClick();
							}
							addForm.getForm().setValues(obj.entity);
						},
						failure : function() {
							Ext.MessageBox.alert('提示 ', '提取数据失败！');
						}
					});
				}
			});
});