"use strict";(globalThis["webpackChunkideahut_quasar_central"]=globalThis["webpackChunkideahut_quasar_central"]||[]).push([[701],{3336:(__unused_webpack_module,__webpack_exports__,__webpack_require__)=>{__webpack_require__.d(__webpack_exports__,{V:()=>grid});var core_js_modules_es_array_push_js__WEBPACK_IMPORTED_MODULE_0__=__webpack_require__(239),core_js_modules_es_array_push_js__WEBPACK_IMPORTED_MODULE_0___default=__webpack_require__.n(core_js_modules_es_array_push_js__WEBPACK_IMPORTED_MODULE_0__),src_scripts_util__WEBPACK_IMPORTED_MODULE_1__=__webpack_require__(9928),src_scripts_uix__WEBPACK_IMPORTED_MODULE_2__=__webpack_require__(8916),src_scripts_api__WEBPACK_IMPORTED_MODULE_3__=__webpack_require__(526);const grid={id:{fromPk:function(e,t){if(src_scripts_util__WEBPACK_IMPORTED_MODULE_1__.Z.isDefined(t))return"STANDARD"===e.type?t:JSON.parse(t)},toPk:function(e,t){let i;if("COMPOSITE"===e.type){i={};for(const l of e.fields)i[l]=t[l];i=JSON.stringify(i)}else"EMBEDDED"===e.type?i=JSON.stringify(t[e.fields[0]]):"STANDARD"===e.type&&(i=t[e.fields[0]]);return i}},prepare:{toFunction:function(elements){if(src_scripts_util__WEBPACK_IMPORTED_MODULE_1__.Z.isArray(elements))for(const element of elements){let format=element.format;src_scripts_util__WEBPACK_IMPORTED_MODULE_1__.Z.isString(format)&&format.startsWith("function")?(format=eval("("+format+")"),element.format=format):delete element.format;let toValue=element.toValue;src_scripts_util__WEBPACK_IMPORTED_MODULE_1__.Z.isString(toValue)&&toValue.startsWith("function")?(toValue=eval("("+toValue+")"),element.toValue=toValue):delete element.toValue;let nullValue=element.nullValue;src_scripts_util__WEBPACK_IMPORTED_MODULE_1__.Z.isString(nullValue)&&nullValue.startsWith("function")?(nullValue=eval("("+nullValue+")"),element.nullValue=nullValue):delete element.nullValue}},options:function(e){src_scripts_util__WEBPACK_IMPORTED_MODULE_1__.Z.isObject(e)&&Object.keys(e).forEach((t=>{let i=grid.get.array(e[t]);for(const e of i)src_scripts_util__WEBPACK_IMPORTED_MODULE_1__.Z.isDefined(e.value)&&null!==e.value||(e.value=void 0);e[t]=i}))},children:function(e){if(src_scripts_util__WEBPACK_IMPORTED_MODULE_1__.Z.isArray(e))for(const t of e){let e=grid.get.array(t.fields);grid.prepare.toFunction(e),t.fields=e;let i=grid.get.object(t.table);grid.prepare.toFunction(i.columns),t.table=i}},forms:function(e){if(src_scripts_util__WEBPACK_IMPORTED_MODULE_1__.Z.isArray(e))for(const t of e){let e=grid.get.array(t.fields);grid.prepare.toFunction(e),t.fields=e}},picks:function(e){src_scripts_util__WEBPACK_IMPORTED_MODULE_1__.Z.isObject(e)&&Object.keys(e).forEach((t=>{let i=grid.get.object(e[t]),l=grid.get.object(i.table);grid.prepare.toFunction(l.columns),i.table=l,e[t]=i}))}},inject:{pkAndGridId:function(e,t,i){if(src_scripts_util__WEBPACK_IMPORTED_MODULE_1__.Z.isArray(t)){let l;if("COMPOSITE"===e.type)for(const s of t){l={};for(const t of e.fields)l[t]=s[t];l=0===Object.keys(l).length?void 0:JSON.stringify(l),s._pk_=l,s._grid_id_=i}else if("EMBEDDED"===e.type)for(const s of t)l=s[e.fields[0]],l=0===Object.keys(l).length?void 0:JSON.stringify(l),s._pk_=l,s._grid_id_=i;else if("STANDARD"===e.type)for(const s of t)s._pk_=s[e.fields[0]],s._grid_id_=i}else if(src_scripts_util__WEBPACK_IMPORTED_MODULE_1__.Z.isObject(t)){let l;if("COMPOSITE"===e.type){l={};for(const i of e.fields)l[i]=t[i];l=0===Object.keys(l).length?void 0:JSON.stringify(l)}else"EMBEDDED"===e.type?(l=t[e.fields[0]],l=0===Object.keys(l).length?void 0:JSON.stringify(l)):"STANDARD"===e.type&&(l=t[e.fields[0]]);t._pk_=l,t._grid_id_=i}}},update:{page:function(e,t,i){if(e.page=t.index,e.rowsPerPage=t.size,src_scripts_util__WEBPACK_IMPORTED_MODULE_1__.Z.isNumber(t.records))e.rowsNumber=t.records;else{let l=t.index*t.size;i!==t.size?e.rowsNumber=l:e.rowsNumber=l+1}}},get:{string:function(e,t){return src_scripts_util__WEBPACK_IMPORTED_MODULE_1__.Z.isString(e)?e:t},number:function(e,t){return src_scripts_util__WEBPACK_IMPORTED_MODULE_1__.Z.isNumber(e)?e:t},object:function(e){return src_scripts_util__WEBPACK_IMPORTED_MODULE_1__.Z.isObject(e)?e:{}},array:function(e){return src_scripts_util__WEBPACK_IMPORTED_MODULE_1__.Z.isArray(e)?e:[]},firstArray:function(e){if(src_scripts_util__WEBPACK_IMPORTED_MODULE_1__.Z.isArray(e)&&e.length>0)return e[0]},pagination(e,t){let i=e?.pagination?e.pagination:t.pagination;return i?(t.pagination=i,i):t.pagination}},copy:function(e){return JSON.parse(JSON.stringify(e))},clone:{field:function(e){let t=grid.copy(e);return t.format=e.format,t.toValue=e.toValue,t.nullValue=e.nullValue,t}},validation:{field:function(e){let t=e.validations;if(src_scripts_util__WEBPACK_IMPORTED_MODULE_1__.Z.isString(t)&&(t=[t]),!src_scripts_util__WEBPACK_IMPORTED_MODULE_1__.Z.isArray(t)||!t.length)return!0;let i,l=src_scripts_util__WEBPACK_IMPORTED_MODULE_1__.Z.isDefined(e.value)&&null!==e.value?e.value+"":"";for(const s of t)if(i=s.trim().toLowerCase(),"required"===i){if(!l.trim().length)return src_scripts_uix__WEBPACK_IMPORTED_MODULE_2__.L.error("error.required",e.label),!1}else if("number"===i){let t=+l;if(isNaN(t))return src_scripts_uix__WEBPACK_IMPORTED_MODULE_2__.L.error("error.fill_with_numbers",e.label),!1}else if("email"===i){if(l=l.trim(),!src_scripts_util__WEBPACK_IMPORTED_MODULE_1__.Z.isEmail(l))return src_scripts_uix__WEBPACK_IMPORTED_MODULE_2__.L.error("error.fill_with_email",e.label),!1;e.value=l}else if(i.startsWith("length:")){let t=+i.substring(7).trim();if(!isNaN(t)&&l.length!==t)return src_scripts_uix__WEBPACK_IMPORTED_MODULE_2__.L.error("error.fill_with_length",e.label,t),!1}else if(i.startsWith("maxlength:")){let t=+i.substring(10).trim();if(!isNaN(t)&&l.length>t)return src_scripts_uix__WEBPACK_IMPORTED_MODULE_2__.L.error("error.fill_with_maxlength",e.label,t),!1}else if(i.startsWith("minlength:")){let t=+i.substring(10).trim();if(!isNaN(t)&&l.length<t)return src_scripts_uix__WEBPACK_IMPORTED_MODULE_2__.L.error("error.fill_with_minlength",e.label,t),!1}return!0}},action:{page:function(e){let t=e.props,i=e.table,{page:l,rowsPerPage:s,sortBy:_,descending:r}=grid.get.pagination(t,i),a=[],n=e.search;if(!n.empty){let e,t;for(const i of n.filters)if(e=src_scripts_util__WEBPACK_IMPORTED_MODULE_1__.Z.isDefined(i.value)?i.value:"",""!==e)if("BETWEEN"===i.condition)t=src_scripts_util__WEBPACK_IMPORTED_MODULE_1__.Z.isDefined(i.value2)?i.value2:"",""!==t&&("datetime"===i.type&&"epoch"===i.converter&&(e=src_scripts_util__WEBPACK_IMPORTED_MODULE_1__.Z.parse.epoch(e,{format:i.pattern||null}),t=src_scripts_util__WEBPACK_IMPORTED_MODULE_1__.Z.parse.epoch(t,{format:i.pattern||null})),a.push({field:i.name,condition:i.condition,values:[e,t]}));else{"datetime"===i.type&&"epoch"===i.converter&&(e=src_scripts_util__WEBPACK_IMPORTED_MODULE_1__.Z.parse.epoch(e,{format:i.pattern||null}));let t=i.or;if(src_scripts_util__WEBPACK_IMPORTED_MODULE_1__.Z.isString[t]&&(t=[t]),t=src_scripts_util__WEBPACK_IMPORTED_MODULE_1__.Z.isArray(t)?t:[],t.length){let l={logical:"and",filters:[]};for(let s=0;s<t.length;s++)l.filters.push({field:t[s],logical:0===s?"and":"or",condition:i.condition,value:e});a.push(l)}else a.push({field:i.name,condition:i.condition,value:e})}}let o=e.relations;if(src_scripts_util__WEBPACK_IMPORTED_MODULE_1__.Z.isArray(o))for(const u of o)a.push({field:u.target,condition:"EQUAL",value:u.value});let c=e.definition,d=e.replica,p=grid.copy(c.crud);if(src_scripts_util__WEBPACK_IMPORTED_MODULE_1__.Z.isNumber(d)&&d>-1){p.replica=d;let t=!0===e.allUseSameReplica;if(t&&src_scripts_util__WEBPACK_IMPORTED_MODULE_1__.Z.isArray(p.joins))for(const e of p.joins)e.replica=p.replica}p.page={index:l,size:s,count:!0===c.table.page.count},p.filters=src_scripts_util__WEBPACK_IMPORTED_MODULE_1__.Z.isArray(p.filters)?p.filters:[],p.filters=p.filters.concat(a),_&&(p.orders=[(r?"-":"")+_]),i.loading=!0,i.selected=[],src_scripts_api__WEBPACK_IMPORTED_MODULE_3__.F.call({path:"/crud/page",method:"post",data:p,onFinish(){i.loading=!1},onSuccess(e){src_scripts_util__WEBPACK_IMPORTED_MODULE_1__.Z.isObject(e)&&(i.rows=grid.get.array(e.data),grid.inject.pkAndGridId(c.id,i.rows,c._grid_id_),grid.update.page(i.pagination,e,i.rows.length),i.pagination.sortBy=_,i.pagination.descending=r)}})},delete:function(e){let t=[];if(e?.row)t.push(grid.id.fromPk(e.definition.id,e.row._pk_));else for(const i of e.table.selected)t.push(grid.id.fromPk(e.definition.id,i._pk_));t.length&&(e.deleting=!1,src_scripts_uix__WEBPACK_IMPORTED_MODULE_2__.L.confirm((function(){let i=grid.copy(e.definition.crud),l="/crud/delete";1===t.length?i.id=t[0]:(i.ids=t,l+="s");let s=e.replica;src_scripts_util__WEBPACK_IMPORTED_MODULE_1__.Z.isNumber(s)&&s>-1&&(i.replica=s),e.deleting=!0,src_scripts_api__WEBPACK_IMPORTED_MODULE_3__.F.call({path:l,method:"post",data:i,onFinish(){e.deleting=!1},onSuccess(t){src_scripts_util__WEBPACK_IMPORTED_MODULE_1__.Z.isFunction(e.onSuccess)&&e.onSuccess()}})}),"confirm.delete"))},save:function(e){let t={};for(const a of e.fields){if(!grid.validation.field(a))return;if("datetime"===a.type&&"epoch"===a.converter)t[a.name]=src_scripts_util__WEBPACK_IMPORTED_MODULE_1__.Z.parse.epoch(a.value,{format:a.pattern||null});else if("pick"===a.type)if(void 0===a.value&&a.nullable)src_scripts_util__WEBPACK_IMPORTED_MODULE_1__.Z.isFunction(a.nullValue)?t[a.name]=a.nullValue():t[a.name]=null;else if(src_scripts_util__WEBPACK_IMPORTED_MODULE_1__.Z.isFunction(a.toValue)&&src_scripts_util__WEBPACK_IMPORTED_MODULE_1__.Z.isObject(a.value)){let e=a.toValue(a.value);Object.keys(e).forEach((i=>{t[i]=e[i]}))}else t[a.name]=a.value;else void 0!==a.value&&null!==a.value||!a.nullable?t[a.name]=a.value:src_scripts_util__WEBPACK_IMPORTED_MODULE_1__.Z.isFunction(a.nullValue)?t[a.name]=a.nullValue():t[a.name]=!0===a.nullEmpty?"":null}let i=e.relations;if(src_scripts_util__WEBPACK_IMPORTED_MODULE_1__.Z.isArray(i))for(const a of i)t[a.target]=a.value;let l=e.definition,s=grid.copy(l.crud);s.value=t;let _,r=e.replica;src_scripts_util__WEBPACK_IMPORTED_MODULE_1__.Z.isNumber(r)&&r>-1&&(s.replica=r),!0===e.is_edit?(s.id=e.id,_="/crud/update"):_="/crud/create",e.saving=!0,src_scripts_api__WEBPACK_IMPORTED_MODULE_3__.F.call({path:_,method:"post",data:s,onFinish(){e.saving=!1},onSuccess(t){t=grid.get.object(t),grid.inject.pkAndGridId(l.id,t,l._grid_id_),src_scripts_util__WEBPACK_IMPORTED_MODULE_1__.Z.isFunction(e.onSuccess)&&e.onSuccess(t)}})}},permission:{view:function(e){let t=src_scripts_util__WEBPACK_IMPORTED_MODULE_1__.Z.isObject(e)?e:{};return src_scripts_util__WEBPACK_IMPORTED_MODULE_1__.Z.isArray(t.actions)&&t.actions.includes("PAGE")},add:function(e){let t=src_scripts_util__WEBPACK_IMPORTED_MODULE_1__.Z.isObject(e)?e:{};return src_scripts_util__WEBPACK_IMPORTED_MODULE_1__.Z.isArray(t.actions)&&t.actions.includes("CREATE")},edit:function(e){let t=src_scripts_util__WEBPACK_IMPORTED_MODULE_1__.Z.isObject(e)?e:{};return src_scripts_util__WEBPACK_IMPORTED_MODULE_1__.Z.isArray(t.actions)&&t.actions.includes("UPDATE")},delete:function(e){let t=src_scripts_util__WEBPACK_IMPORTED_MODULE_1__.Z.isObject(e)?e:{};return src_scripts_util__WEBPACK_IMPORTED_MODULE_1__.Z.isArray(t.actions)&&t.actions.includes("DELETE")},deletes:function(e){let t=src_scripts_util__WEBPACK_IMPORTED_MODULE_1__.Z.isObject(e)?e:{};return src_scripts_util__WEBPACK_IMPORTED_MODULE_1__.Z.isArray(t.actions)&&t.actions.includes("DELETES")}}}},701:(e,t,i)=>{i.r(t),i.d(t,{default:()=>T});var l=i(1758),s=i(8790);const _={class:"full-width row flex-center text-accent q-gutter-sm"},r={class:"text-subtitle2"},a={key:0,class:"text-left"},n={class:"text-caption"};function o(e,t,i,o,c,d){const p=(0,l.g2)("q-tooltip"),u=(0,l.g2)("q-btn"),g=(0,l.g2)("q-space"),m=(0,l.g2)("q-icon"),f=(0,l.g2)("q-item-label"),E=(0,l.g2)("q-item-section"),b=(0,l.g2)("q-item"),O=(0,l.g2)("q-select"),D=(0,l.g2)("q-badge"),P=(0,l.g2)("q-inner-loading"),h=(0,l.g2)("q-checkbox"),M=(0,l.g2)("q-input"),k=(0,l.g2)("q-table"),y=(0,l.g2)("q-spinner-gears"),A=(0,l.g2)("Search"),w=(0,l.g2)("q-dialog"),C=(0,l.g2)("View"),v=(0,l.g2)("Edit");return(0,l.uX)(),(0,l.CE)(l.FK,null,[(0,l.bF)(k,{class:"table-sticky-header q-ma-sm",rows:o.table.rows,columns:o.table.columns,"visible-columns":o.table.visibles,"row-key":"_pk_",loading:o.table.loading,selection:o.util.isObject(o.template.table)?o.template.table.selection:"single",selected:o.table.selected,"onUpdate:selected":t[2]||(t[2]=e=>o.table.selected=e),pagination:o.table.pagination,"onUpdate:pagination":t[3]||(t[3]=e=>o.table.pagination=e),dense:e.$q.screen.lt.md,"no-data-label":e.$t("error.data_not_available"),"rows-per-page-label":" ","selected-rows-label":e=>{},"rows-per-page-options":o.util.isObject(o.template.table)&&o.template.table.page?.options?o.template.table.page.options:[10,20,30],onRequest:d.do_request,"binary-state-sort":"",separator:o.util.isObject(o.template.table)?o.template.table.separator:"cell",bordered:""},{top:(0,l.k6)((()=>[o.util.isObject(o.template.table)&&"multiple"===o.template.table.selection&&o.fxGrid.permission.delete(o.permission)?((0,l.uX)(),(0,l.Wv)(u,{key:0,glossy:"",round:"",dense:"",class:"q-ma-none q-mr-md",color:"negative",icon:"delete",disable:!o.table.selected.length,loading:o.table.deleting,onClick:d.on_delete_click},{default:(0,l.k6)((()=>[(0,l.bF)(p,null,{default:(0,l.k6)((()=>[(0,l.eW)((0,s.v_)(e.$t("label.delete")),1)])),_:1})])),_:1},8,["disable","loading","onClick"])):(0,l.Q3)("",!0),o.fxGrid.permission.add(o.permission)?((0,l.uX)(),(0,l.Wv)(u,{key:1,glossy:"",round:"",dense:"",class:"q-ma-none q-mr-md",color:"teal",icon:"add",onClick:d.on_add_click},{default:(0,l.k6)((()=>[(0,l.bF)(p,null,{default:(0,l.k6)((()=>[(0,l.eW)((0,s.v_)(e.$t("label.new")),1)])),_:1})])),_:1},8,["onClick"])):(0,l.Q3)("",!0),(0,l.bF)(g),o.template.replicas?.length?((0,l.uX)(),(0,l.Wv)(O,{key:2,modelValue:o.replica,"onUpdate:modelValue":[t[0]||(t[0]=e=>o.replica=e),d.do_load_data],options:o.template.replicas,borderless:"",class:"text-h6",dense:""},{prepend:(0,l.k6)((()=>[(0,l.bF)(m,{name:"storage",class:"q-mr-sm"})])),option:(0,l.k6)((e=>[(0,l.bF)(b,(0,s._B)((0,l.Ng)(e.itemProps)),{default:(0,l.k6)((()=>[(0,l.bF)(E,null,{default:(0,l.k6)((()=>[(0,l.bF)(f,{class:"text-h6"},{default:(0,l.k6)((()=>[(0,l.eW)((0,s.v_)(e.label),1)])),_:2},1024)])),_:2},1024)])),_:2},1040)])),_:1},8,["modelValue","options","onUpdate:modelValue"])):(0,l.Q3)("",!0),(0,l.bF)(g),o.search.filters?.length?((0,l.uX)(),(0,l.Wv)(u,{key:3,glossy:"",round:"",dense:"",class:"q-ma-none q-ml-md",color:"deep-orange",icon:"search",onClick:d.on_search_click},{default:(0,l.k6)((()=>[o.search.empty?(0,l.Q3)("",!0):((0,l.uX)(),(0,l.Wv)(D,{key:0,class:"led-green",floating:""})),(0,l.bF)(p,null,{default:(0,l.k6)((()=>[(0,l.eW)((0,s.v_)(e.$t("label.search")),1)])),_:1})])),_:1},8,["onClick"])):(0,l.Q3)("",!0),(0,l.bF)(u,{glossy:"",round:"",dense:"",class:"q-ma-none q-ml-md",color:"indigo",icon:"refresh",loading:o.table.loading,onClick:d.on_refresh_click},{default:(0,l.k6)((()=>[(0,l.bF)(p,null,{default:(0,l.k6)((()=>[(0,l.eW)((0,s.v_)(e.$t("label.refresh")),1)])),_:1})])),_:1},8,["loading","onClick"])])),"no-data":(0,l.k6)((({message:e})=>[(0,l.Lk)("div",_,[(0,l.bF)(m,{size:"2em",name:"block"}),(0,l.Lk)("span",r,(0,s.v_)(e),1)])])),loading:(0,l.k6)((()=>[(0,l.bF)(P,{showing:"",color:"primary"})])),"header-selection":(0,l.k6)((e=>[o.util.isObject(o.template.table)&&"multiple"===o.template.table.selection&&o.fxGrid.permission.deletes(o.permission)?((0,l.uX)(),(0,l.CE)("div",a,[(0,l.bF)(h,{dense:"",color:"primary",class:"q-ma-none q-ml-sm q-mr-sm",modelValue:e.selected,"onUpdate:modelValue":t=>e.selected=t},null,8,["modelValue","onUpdate:modelValue"])])):(0,l.Q3)("",!0)])),"body-selection":(0,l.k6)((t=>[o.util.isObject(o.template.table)&&"multiple"===o.template.table.selection&&o.fxGrid.permission.deletes(o.permission)?((0,l.uX)(),(0,l.Wv)(h,{key:0,dense:"",color:"primary",class:"q-ma-none q-ml-sm q-mr-sm",modelValue:t.selected,"onUpdate:modelValue":e=>t.selected=e},null,8,["modelValue","onUpdate:modelValue"])):o.util.isObject(o.template.table)&&o.fxGrid.permission.delete(o.permission)?((0,l.uX)(),(0,l.Wv)(u,{key:1,glossy:"",round:"",dense:"",size:"sm",class:"q-ma-none q-ml-xs q-mr-sm",color:"negative",icon:"delete",onClick:e=>d.on_delete_click(t)},{default:(0,l.k6)((()=>[(0,l.bF)(p,null,{default:(0,l.k6)((()=>[(0,l.eW)((0,s.v_)(e.$t("label.delete")),1)])),_:1})])),_:2},1032,["onClick"])):(0,l.Q3)("",!0),o.fxGrid.permission.edit(o.permission)?((0,l.uX)(),(0,l.Wv)(u,{key:2,glossy:"",round:"",dense:"",size:"sm",class:"q-ma-none q-ml-xs q-mr-sm",color:"primary",icon:"drive_file_rename_outline",onClick:e=>d.on_edit_click(t)},{default:(0,l.k6)((()=>[(0,l.bF)(p,null,{default:(0,l.k6)((()=>[(0,l.eW)((0,s.v_)(e.$t("label.edit")),1)])),_:1})])),_:2},1032,["onClick"])):(0,l.Q3)("",!0),o.fxGrid.permission.view(o.permission)?((0,l.uX)(),(0,l.Wv)(u,{key:3,glossy:"",round:"",dense:"",size:"sm",class:"q-ma-none q-ml-xs q-mr-sm",color:"deep-purple",icon:"visibility",onClick:e=>d.on_view_click(t)},{default:(0,l.k6)((()=>[(0,l.bF)(p,null,{default:(0,l.k6)((()=>[(0,l.eW)((0,s.v_)(e.$t("label.view")),1)])),_:1})])),_:2},1032,["onClick"])):(0,l.Q3)("",!0)])),pagination:(0,l.k6)((e=>[e.pagesNumber>2?((0,l.uX)(),(0,l.Wv)(u,{key:0,glossy:"",size:"sm",icon:"first_page",color:"grey-8",round:"",dense:"",flat:"",disable:e.isFirstPage,onClick:e.firstPage},null,8,["disable","onClick"])):(0,l.Q3)("",!0),(0,l.bF)(u,{glossy:"",size:"sm",icon:"chevron_left",color:"grey-8",round:"",dense:"",flat:"",disable:e.isFirstPage,onClick:e.prevPage},null,8,["disable","onClick"]),(0,l.bF)(M,{dense:"",borderless:"",type:"number","input-class":"text-center q-ml-xs q-mr-xs","input-style":"max-width: 60px;",modelValue:o.table.pagination.page,"onUpdate:modelValue":[t[1]||(t[1]=e=>o.table.pagination.page=e),d.on_page_changed]},(0,l.eX)({_:2},[o.util.isObject(o.template.table)&&o.util.isObject(o.template.table.page)&&!0===o.template.table.page.count?{name:"append",fn:(0,l.k6)((()=>[(0,l.Lk)("span",n," / "+(0,s.v_)(e.pagesNumber),1)])),key:"0"}:void 0]),1032,["modelValue","onUpdate:modelValue"]),(0,l.bF)(u,{glossy:"",size:"sm",icon:"chevron_right",color:"grey-8",round:"",dense:"",flat:"",disable:e.isLastPage,onClick:e.nextPage},null,8,["disable","onClick"]),e.pagesNumber>2?((0,l.uX)(),(0,l.Wv)(u,{key:1,glossy:"",size:"sm",icon:"last_page",color:"grey-8",round:"",dense:"",flat:"",disable:e.isLastPage,onClick:e.lastPage},null,8,["disable","onClick"])):(0,l.Q3)("",!0)])),_:1},8,["rows","columns","visible-columns","loading","selection","selected","pagination","dense","no-data-label","rows-per-page-options","onRequest","separator"]),(0,l.bF)(P,{showing:o.is_template_loading},{default:(0,l.k6)((()=>[(0,l.bF)(y,{size:"80px",color:"primary"})])),_:1},8,["showing"]),(0,l.bF)(w,{modelValue:o.dialog.search.show,"onUpdate:modelValue":t[4]||(t[4]=e=>o.dialog.search.show=e),persistent:"","transition-show":"slide-down","transition-hide":"none"},{default:(0,l.k6)((()=>[(0,l.bF)(A,{parameters:o.dialog.search.parameters,onClose:d.on_close_dialog_search},null,8,["parameters","onClose"])])),_:1},8,["modelValue"]),(0,l.bF)(w,{modelValue:o.dialog.view.show,"onUpdate:modelValue":t[5]||(t[5]=e=>o.dialog.view.show=e),persistent:"","transition-show":"slide-down","transition-hide":"none"},{default:(0,l.k6)((()=>[(0,l.bF)(C,{parameters:o.dialog.view.parameters},null,8,["parameters"])])),_:1},8,["modelValue"]),(0,l.bF)(w,{modelValue:o.dialog.edit.show,"onUpdate:modelValue":t[6]||(t[6]=e=>o.dialog.edit.show=e),persistent:"","transition-show":"slide-down","transition-hide":"none"},{default:(0,l.k6)((()=>[(0,l.bF)(v,{parameters:o.dialog.edit.parameters,onClose:d.on_close_dialog_edit},null,8,["parameters","onClose"])])),_:1},8,["modelValue"])],64)}var c=i(8734),d=i(9928),p=i(8916),u=i(526),g=i(3336);window.__util__=d.Z,window.__grid__={};const m={components:{Search:(0,l.$V)((()=>Promise.all([i.e(121),i.e(996)]).then(i.bind(i,8991)))),View:(0,l.$V)((()=>Promise.all([i.e(121),i.e(191)]).then(i.bind(i,2191)))),Edit:(0,l.$V)((()=>Promise.all([i.e(121),i.e(282)]).then(i.bind(i,1901))))},setup(){return{uix:p.L,util:d.Z,fxGrid:g.V,is_template_loading:(0,c.KR)(!1),template:(0,c.KR)({}),permission:(0,c.KR)({}),parent:(0,c.KR)(null),name:(0,c.KR)(null),replica:(0,c.KR)(null),dialog:(0,c.KR)({search:{show:!1,parameters:null},view:{show:!1,parameters:null},edit:{show:!1,parameters:null}}),search:(0,c.KR)({filters:[],empty:!0}),table:(0,c.KR)({rows:[],columns:[],visibles:[],selected:[],pagination:{},loading:!1,deleting:!1})}},created(){this.do_load_grid()},beforeUpdate(){this.do_load_grid()},methods:{do_load_grid(){d.Z.isObject(window.__grid__)||(window.__grid__={});let e=this,t=g.V.get.string(e.$route.query.parent,"_"),i=g.V.get.string(e.$route.query.name,"");if(t===e.parent&&i===e.name)return;e.parent=t,e.name=i,e.replica=null;let l=t+"_"+i;if(window.__grid__[l]){e.template=window.__grid__[l],e.replica=g.V.get.firstArray(e.template.replicas),e.permission=e.get_permission();try{e.do_load_data()}catch(s){d.Z.log("<<get-grid-2::"+l+">>",s)}}else e.is_template_loading||(e.is_template_loading=!0,u.F.call({path:"/grid",params:{name:i,parent:t},onSuccess(t){try{let i=g.V.get.object(t);i._grid_id_=l;let s=g.V.get.object(i.table);g.V.prepare.toFunction(s.columns),i.table=s;let _=g.V.get.array(i.fields);g.V.prepare.toFunction(_),i.fields=_;let r=g.V.get.array(i.children);g.V.prepare.children(r),i.children=r;let a=g.V.get.object(i.enums);g.V.prepare.options(a),i.enums=a;let n=g.V.get.object(i.options);g.V.prepare.options(n),i.options=n;let o=g.V.get.array(i.forms);g.V.prepare.forms(o),i.forms=o;let c=g.V.get.object(i.picks);g.V.prepare.picks(c),i.picks=c,window.__grid__[l]=i,e.template=window.__grid__[l],e.replica=g.V.get.firstArray(e.template.replicas),e.permission=e.get_permission(),e.do_load_data()}catch(s){d.Z.log("<<get-grid-1::"+l+">>",s)}e.is_template_loading=!1},notify:!0,onError(t){e.is_template_loading=!1}}))},get_permission(){let e=this,t=e.template,i=g.V.get.object(t.table),l={actions:g.V.copy(t.actions)},s=i?.action?.exclude?i.action.exclude:[];return l.actions=t.actions.filter((e=>!s.includes(e))),l},do_load_data(){let e=this;e.table={rows:[],columns:[{name:"_pk_"}],visibles:["_pk_"],selected:[],pagination:{},loading:!1,deleting:!1};let t=e.template,i=g.V.get.object(t.table),l=g.V.get.object(i.page),s=g.V.get.object(i.order);e.search={empty:!0,filters:g.V.copy(g.V.get.array(i.filters))},e.table.pagination={page:1,rowsPerPage:l.default,sortBy:s.field,descending:!0===s.descending},e.table.columns=i.columns,e.table.visibles=i.visibles,e.do_request({pagination:e.table.pagination})},do_request(e){let t=this;g.V.action.page({props:e,table:t.table,search:t.search,definition:t.template,replica:t.replica,allUseSameReplica:t.template.allUseSameReplica})},on_refresh_click(){let e=this;e.do_request({pagination:e.table.pagination})},on_delete_click(e){let t=this;g.V.action.delete({row:e.row,definition:t.template,table:t.table,deleting:t.deleting,replica:t.replica,onSuccess:function(){t.do_request({pagination:t.table.pagination})}})},on_search_click(){let e=this,t=e.dialog.search;t.parameters={filters:g.V.copy(e.search.filters),template:e.template},t.show=!0},on_close_dialog_search(e){let t=this;if(d.Z.isArray(e)){let i,l,s=t.search;s.filters=e,s.empty=!0;for(const e of s.filters)if(i=d.Z.isDefined(e.value)?e.value:"",l=d.Z.isDefined(e.value2)?e.value2:"",""!==i||""!==l){s.empty=!1;break}t.dialog.search={show:!1,parameters:null},t.table.pagination.page=1,t.do_request({pagination:t.table.pagination})}},on_view_click(e){let t=this;t.dialog.view={show:!0,parameters:{template:t.template,replica:t.replica,row:e.row}}},on_edit_click(e){let t=this;t.dialog.edit={show:!0,parameters:{template:t.template,replica:t.replica,row:e.row,index:e.rowIndex}}},on_add_click(){let e=this;e.dialog.edit={show:!0,parameters:{template:e.template,replica:e.replica}}},on_close_dialog_edit(e){let t=this,i=e.row;if(i)if(e.is_edit){if(!d.Z.isDefined(i._pk_))return t.dialog.edit={show:!1,parameters:null},void setTimeout((function(){t.dialog.edit={show:!0,parameters:{template:t.template,replica:t.replica,row:i}}}),500);t.table.rows[e.index]=i}else t.do_request({pagination:t.table.pagination});t.dialog.edit={show:!1,parameters:null}},on_page_changed(){let e=this,t=+e.table.pagination.page;!isNaN(t)&&t>0&&e.do_request({pagination:e.table.pagination})}}};var f=i(2807),E=i(481),b=i(9001),O=i(8151),D=i(1173),P=i(833),h=i(4609),M=i(5329),k=i(3418),y=i(5779),A=i(4031),w=i(4264),C=i(2225),v=i(8020),W=i(7839),L=i(7201),R=i(8582),U=i.n(R);const V=(0,f.A)(m,[["render",o]]),T=V;U()(m,"components",{QTable:E.A,QBtn:b.A,QTooltip:O.A,QSpace:D.A,QSelect:P.A,QIcon:h.A,QItem:M.A,QItemSection:k.A,QItemLabel:y.A,QBadge:A.A,QInnerLoading:w.A,QCheckbox:C.A,QInput:v.A,QSpinnerGears:W.A,QDialog:L.A})}}]);