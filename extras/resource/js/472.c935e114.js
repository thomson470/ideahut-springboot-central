"use strict";(globalThis["webpackChunkideahut_quasar_central"]=globalThis["webpackChunkideahut_quasar_central"]||[]).push([[472],{9472:(e,a,l)=>{l.r(a),l.d(a,{default:()=>W});var t=l(1758),s=l(8790);const i={class:"full-width row flex-center text-accent q-gutter-sm"},o={class:"text-subtitle2"},n={class:"text-caption"},r={class:"col-6 q-pr-xs text-left"},c={class:"col-6 q-pl-xs text-right"};function p(e,a,l,p,d,g){const b=(0,t.g2)("q-item-label"),u=(0,t.g2)("q-item-section"),m=(0,t.g2)("q-tooltip"),k=(0,t.g2)("q-btn"),h=(0,t.g2)("q-item"),_=(0,t.g2)("q-card-section"),f=(0,t.g2)("q-space"),q=(0,t.g2)("q-badge"),y=(0,t.g2)("q-icon"),v=(0,t.g2)("q-inner-loading"),w=(0,t.g2)("q-radio"),F=(0,t.g2)("q-input"),x=(0,t.g2)("q-table"),C=(0,t.g2)("q-separator"),A=(0,t.g2)("q-card-actions"),Q=(0,t.g2)("q-card"),V=(0,t.g2)("Search"),R=(0,t.g2)("q-dialog"),P=(0,t.gN)("close-popup");return(0,t.uX)(),(0,t.CE)(t.FK,null,[(0,t.bF)(Q,null,{default:(0,t.k6)((()=>[(0,t.bF)(_,{class:"q-pa-none header-main"},{default:(0,t.k6)((()=>[(0,t.bF)(h,{class:"q-pr-none"},{default:(0,t.k6)((()=>[(0,t.bF)(u,null,{default:(0,t.k6)((()=>[(0,t.bF)(b,{class:"text-h6 text-white"},{default:(0,t.k6)((()=>[(0,t.eW)((0,s.v_)(p.pick.title),1)])),_:1})])),_:1}),(0,t.bF)(u,{side:""},{default:(0,t.k6)((()=>[(0,t.bo)(((0,t.uX)(),(0,t.Wv)(k,{class:"text-caption text-white q-pl-xs q-pr-xs q-mr-xs",flat:"",round:"",glossy:"",icon:"close"},{default:(0,t.k6)((()=>[(0,t.bF)(m,null,{default:(0,t.k6)((()=>[(0,t.eW)((0,s.v_)(e.$t("label.close")),1)])),_:1})])),_:1})),[[P]])])),_:1})])),_:1})])),_:1}),(0,t.bF)(_,{style:{"max-height":"80vh"},class:"q-pa-xs q-pt-sm scroll"},{default:(0,t.k6)((()=>[(0,t.bF)(x,{class:"table-sticky-header q-ma-none",rows:p.table.rows,columns:p.table.columns,"visible-columns":p.table.visibles,"row-key":"_pk_",loading:p.table.loading,selection:"single",pagination:p.table.pagination,"onUpdate:pagination":a[2]||(a[2]=e=>p.table.pagination=e),dense:e.$q.screen.lt.md,"no-data-label":e.$t("error.data_not_available"),"rows-per-page-label":" ","selected-rows-label":e=>{},"rows-per-page-options":p.util.isObject(p.pick.table)&&p.pick.table.page?.options?p.pick.table.page.options:[10,20,30],onRequest:g.do_request,"binary-state-sort":"",separator:p.util.isObject(p.pick.table)?p.pick.table.separator:"cell",bordered:"",style:{"max-height":"70vh"}},{top:(0,t.k6)((()=>[(0,t.bF)(f),p.search.filters&&p.search.filters.length?((0,t.uX)(),(0,t.Wv)(k,{key:0,glossy:"",round:"",dense:"",class:"q-ma-none q-ml-md",color:"deep-orange",icon:"search",onClick:g.on_search_click},{default:(0,t.k6)((()=>[p.search.empty?(0,t.Q3)("",!0):((0,t.uX)(),(0,t.Wv)(q,{key:0,class:"led-green",floating:""}))])),_:1},8,["onClick"])):(0,t.Q3)("",!0),(0,t.bF)(k,{glossy:"",round:"",dense:"",class:"q-ma-none q-ml-md",color:"indigo",icon:"refresh",loading:p.table.loading,onClick:g.on_refresh_click},null,8,["loading","onClick"])])),"no-data":(0,t.k6)((({message:e})=>[(0,t.Lk)("div",i,[(0,t.bF)(y,{size:"2em",name:"block"}),(0,t.Lk)("span",o,(0,s.v_)(e),1)])])),loading:(0,t.k6)((()=>[(0,t.bF)(v,{showing:"",color:"primary"})])),"body-selection":(0,t.k6)((e=>[(0,t.bF)(w,{modelValue:p.table.selected,"onUpdate:modelValue":a[0]||(a[0]=e=>p.table.selected=e),val:e.row,color:"primary"},null,8,["modelValue","val"])])),pagination:(0,t.k6)((e=>[e.pagesNumber>2?((0,t.uX)(),(0,t.Wv)(k,{key:0,glossy:"",size:"sm",icon:"first_page",color:"grey-8",round:"",dense:"",flat:"",disable:e.isFirstPage,onClick:e.firstPage},null,8,["disable","onClick"])):(0,t.Q3)("",!0),(0,t.bF)(k,{glossy:"",size:"sm",icon:"chevron_left",color:"grey-8",round:"",dense:"",flat:"",disable:e.isFirstPage,onClick:e.prevPage},null,8,["disable","onClick"]),(0,t.bF)(F,{dense:"",borderless:"",type:"number","input-class":"text-center q-ml-xs q-mr-xs","input-style":"max-width: 60px;",modelValue:p.table.pagination.page,"onUpdate:modelValue":[a[1]||(a[1]=e=>p.table.pagination.page=e),g.on_page_changed]},(0,t.eX)({_:2},[p.util.isObject(p.pick.table)&&p.util.isObject(p.pick.table.page)&&!0===p.pick.table.page.count?{name:"append",fn:(0,t.k6)((()=>[(0,t.Lk)("span",n," / "+(0,s.v_)(e.pagesNumber),1)])),key:"0"}:void 0]),1032,["modelValue","onUpdate:modelValue"]),(0,t.bF)(k,{glossy:"",size:"sm",icon:"chevron_right",color:"grey-8",round:"",dense:"",flat:"",disable:e.isLastPage,onClick:e.nextPage},null,8,["disable","onClick"]),e.pagesNumber>2?((0,t.uX)(),(0,t.Wv)(k,{key:1,glossy:"",size:"sm",icon:"last_page",color:"grey-8",round:"",dense:"",flat:"",disable:e.isLastPage,onClick:e.lastPage},null,8,["disable","onClick"])):(0,t.Q3)("",!0)])),_:1},8,["rows","columns","visible-columns","loading","pagination","dense","no-data-label","rows-per-page-options","onRequest","separator"])])),_:1}),(0,t.bF)(C),(0,t.bF)(A,{class:"row"},{default:(0,t.k6)((()=>[(0,t.Lk)("div",r,[(0,t.bo)((0,t.bF)(k,{label:e.$t("label.cancel"),color:"negative","no-caps":"",glossy:""},null,8,["label"]),[[P]])]),(0,t.Lk)("div",c,[(0,t.bF)(k,{label:e.$t("label.select"),color:"indigo","no-caps":"",glossy:"",disable:!p.table.selected?._pk_,onClick:g.on_select_click},null,8,["label","disable","onClick"])])])),_:1})])),_:1}),(0,t.bF)(R,{modelValue:p.dialog.search.show,"onUpdate:modelValue":a[3]||(a[3]=e=>p.dialog.search.show=e),persistent:"","transition-show":"slide-down","transition-hide":"none"},{default:(0,t.k6)((()=>[(0,t.bF)(V,{parameters:p.dialog.search.parameters,onClose:g.on_close_dialog_search},null,8,["parameters","onClose"])])),_:1},8,["modelValue"])],64)}var d=l(8734),g=l(9928),b=l(3336);const u={props:["parameters"],emits:["close"],components:{Search:(0,t.$V)((()=>Promise.all([l.e(121),l.e(996)]).then(l.bind(l,8991))))},setup(){return{util:g.Z,template:(0,d.KR)({}),field:(0,d.KR)({}),pick:(0,d.KR)({}),relations:(0,d.KR)([]),replica:(0,d.KR)(null),table:(0,d.KR)({rows:[],columns:[],visibles:[],selected:null,pagination:{},loading:!1}),search:(0,d.KR)({filters:[],empty:!0}),dialog:(0,d.KR)({search:{show:!1,parameters:null}})}},created(){let e=this,a=b.V.get.object(e.parameters);e.template=b.V.get.object(a.template),e.replica=b.V.get.number(a.replica,null),e.field=b.V.get.object(a.field),e.relations=b.V.get.array(a.relations),e.pick=b.V.get.object(a.pick),e.pick._grid_id_=e.template._grid_id_,e.search={empty:!0,filters:b.V.copy(b.V.get.array(e.pick.table.filters))},e.table={rows:[],columns:e.pick.table.columns,visibles:e.pick.table.visibles,selected:null,pagination:{page:1,rowsPerPage:e.pick.table.page.default,sortBy:e.pick.table.order.field,descending:!0===e.pick.table.order.descending},loading:!1},e.do_request({pagination:e.table.pagination})},methods:{do_request(e){let a=this,l=null;!0===a.template.allUseSameReplica&&(l=a.replica),b.V.action.page({props:e,table:a.table,search:a.search,definition:a.pick,relations:a.relations,replica:l,allUseSameReplica:a.template.allUseSameReplica})},on_refresh_click(){let e=this;e.do_request({pagination:e.table.pagination})},on_select_click(){let e=this;e.$emit("close",e.table.selected)},on_search_click(){let e=this,a=e.dialog.search;a.parameters={filters:b.V.copy(e.search.filters),template:e.template},a.show=!0},on_close_dialog_search(e){let a=this;if(g.Z.isArray(e)){let l,t,s=a.search;s.filters=e,s.empty=!0;for(const e of s.filters)if(l=g.Z.isDefined(e.value)?e.value:"",t=g.Z.isDefined(e.value2)?e.value2:"",""!==l||""!==t){s.empty=!1;break}a.dialog.search={show:!1,parameters:null},a.table.pagination.page=1,a.do_request({pagination:a.table.pagination})}},on_page_changed(){let e=this,a=+e.table.pagination.page;!isNaN(a)&&a>0&&e.do_request({pagination:e.table.pagination})}}};var m=l(2807),k=l(7569),h=l(7066),_=l(5329),f=l(3418),q=l(5779),y=l(9001),v=l(8151),w=l(481),F=l(1173),x=l(4031),C=l(4609),A=l(4264),Q=l(6883),V=l(8020),R=l(7015),P=l(6334),K=l(7201),L=l(2769),S=l(8582),j=l.n(S);const U=(0,m.A)(u,[["render",p]]),W=U;j()(u,"components",{QCard:k.A,QCardSection:h.A,QItem:_.A,QItemSection:f.A,QItemLabel:q.A,QBtn:y.A,QTooltip:v.A,QTable:w.A,QSpace:F.A,QBadge:x.A,QIcon:C.A,QInnerLoading:A.A,QRadio:Q.A,QInput:V.A,QSeparator:R.A,QCardActions:P.A,QDialog:K.A}),j()(u,"directives",{ClosePopup:L.A})}}]);