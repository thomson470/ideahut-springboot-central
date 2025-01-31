import{Q as T,b as $,a as N}from"./format-ytY8laSO.js";import{Q as A}from"./QTooltip-CbCntXZw.js";import{_ as D,Y as I,Z as x,a0 as F,r as W,a4 as s,a2 as n,a5 as o,f as a,an as S,ab as B,a7 as P,a8 as c,C as y,a6 as m,ac as r,F as j,ad as L,ak as p,G as U,ae as d,aa as u,Q as b,aK as R,ao as z}from"./index-G1u9Lejy.js";import{Q as w,a as V,b as q,c as h}from"./QTabPanels-DMAYU8lg.js";import{Q as g,a as Q,b as C}from"./QPopupProxy-CjwWH78E.js";import{Q as G}from"./QForm-CX1M49Px.js";import{C as _}from"./ClosePopup-CT9nyDbK.js";import"./QResizeObserver-CYxcUGkk.js";import"./QSelect-C3g-BVo1.js";import"./TouchPan-Bp6kdtwJ.js";let i;const K={props:["parameters"],emits:["close"],setup(){return{APP:I,util:x,uix:F,filters:W([])}},created(){i=this;let t=x.isObject(i.parameters)?i.parameters:{};i.filters=x.isArray(t.filters)?t.filters:[]},methods:{on_reset_click(){for(const t of i.filters)delete t.value,delete t.value2},on_filter_click(){for(const t of i.filters)x.isDefined(t.value)&&t.value===null&&delete t.value,x.isDefined(t.value2)&&t.value2===null&&delete t.value2;i.$emit("close",i.filters)}}},O={key:0},Y={key:0,class:"q-mb-xs row"},Z={key:1,class:"q-mb-xs"},H={key:0,class:"row"},J={class:"bg-primary"},M={class:"q-pa-xs bg-white row"},X={class:"col-6 text-left"},f={class:"col-6 text-right"},ee={class:"bg-primary"},ae={class:"q-pa-xs bg-white row"},le={class:"col-6 text-left"},oe={class:"col-6 text-right"},te={class:"bg-primary"},se={class:"q-pa-xs bg-white row"},de={class:"col-6 text-left"},ne={class:"col-6 text-right"},ue={key:2,class:"q-mb-xs"},me={key:0,class:"row"},pe={class:"col-6 q-pr-xs text-left"},ce={class:"col-6 q-pl-xs text-right"};function re(t,ie,ye,v,be,k){return s(),n(z,{style:B("width: "+(t.$q.screen.lt.md?"100%;":"50%;"))},{default:o(()=>[a(S,{class:"q-pa-none header-main",style:B(v.APP?.color?.header?"background: "+v.APP.color.header+" !important;":"")},{default:o(()=>[a(T,{class:"q-pr-none"},{default:o(()=>[a($,null,{default:o(()=>[a(N,{class:"text-h6 text-white"},{default:o(()=>[P(c(t.$t("label.search")),1)]),_:1})]),_:1}),a($,{side:""},{default:o(()=>[y((s(),n(m,{class:"text-caption text-white q-pl-xs q-pr-xs q-mr-xs",flat:"",round:"",glossy:"",icon:"close"},{default:o(()=>[a(A,null,{default:o(()=>[P(c(t.$t("label.close")),1)]),_:1})]),_:1})),[[_]])]),_:1})]),_:1})]),_:1},8,["style"]),a(S,{style:{"max-height":"70vh"},class:"q-pa-xs q-mt-xs scroll"},{default:o(()=>[(s(!0),r(j,null,L(v.filters,(e,E)=>(s(),r("div",{key:E,class:"q-mb-xs"},[a(G,{onSubmit:k.on_filter_click,onReset:k.on_reset_click},{default:o(()=>[e.type==="words"?(s(),r("div",O,[e.condition==="BETWEEN"?(s(),r("div",Y,[a(p,{type:"text",label:e.label,modelValue:e.value,"onUpdate:modelValue":l=>e.value=l,filled:"",autogrow:"",class:"col-6 q-pr-xs",style:{"max-height":"200px",overflow:"scroll"}},null,8,["label","modelValue","onUpdate:modelValue"]),a(p,{type:"text",label:e.label,modelValue:e.value2,"onUpdate:modelValue":l=>e.value2=l,filled:"",autogrow:"",class:"col-6 q-pl-xs",style:{"max-height":"200px",overflow:"scroll"}},null,8,["label","modelValue","onUpdate:modelValue"])])):(s(),n(p,{key:1,type:"text",label:e.label,modelValue:e.value,"onUpdate:modelValue":l=>e.value=l,filled:"",autogrow:"",style:{"max-height":"200px",overflow:"scroll"}},null,8,["label","modelValue","onUpdate:modelValue"]))])):e.type==="datetime"||e.type==="date"||e.type==="time"?(s(),r("div",Z,[e.condition==="BETWEEN"?(s(),r("div",H,[a(p,{type:"text",label:e.label,modelValue:e.value,"onUpdate:modelValue":l=>e.value=l,filled:"",class:"col-6 q-pr-xs"},{append:o(()=>[a(U,{name:e.type==="time"?"schedule":e.type==="date"?"calendar_month":"event",class:"cursor-pointer"},{default:o(()=>[a(g,{"transition-show":"scale","transition-hide":"scale",cover:"",onBeforeShow:l=>v.uix.calendar.beforeShow(e,"tab","proxy_value","value")},{default:o(()=>[d("div",J,[a(w,{modelValue:e.tab,"onUpdate:modelValue":l=>e.tab=l,class:"bg-primary text-grey-5 shadow-2 text-subtitle2",align:"justify","no-caps":"","indicator-color":"transparent","active-color":"white"},{default:o(()=>[e.type==="datetime"||e.type==="date"?(s(),n(V,{key:0,name:"date"},{default:o(()=>[d("span",null,c(t.$t("label.date")),1)]),_:1})):u("",!0),e.type==="datetime"||e.type==="time"?(s(),n(V,{key:1,name:"time"},{default:o(()=>[d("span",null,c(t.$t("label.time")),1)]),_:1})):u("",!0)]),_:2},1032,["modelValue","onUpdate:modelValue"]),a(b),a(q,{modelValue:e.tab,"onUpdate:modelValue":l=>e.tab=l},{default:o(()=>[e.type==="datetime"||e.type==="date"?(s(),n(h,{key:0,name:"date",class:"q-pa-none"},{default:o(()=>[a(Q,{modelValue:e.proxy_value,"onUpdate:modelValue":l=>e.proxy_value=l,mask:e.pattern,square:""},null,8,["modelValue","onUpdate:modelValue","mask"])]),_:2},1024)):u("",!0),e.type==="datetime"||e.type==="time"?(s(),n(h,{key:1,name:"time",class:"q-pa-none"},{default:o(()=>[a(C,{modelValue:e.proxy_value,"onUpdate:modelValue":l=>e.proxy_value=l,mask:e.pattern,format24h:"",square:""},null,8,["modelValue","onUpdate:modelValue","mask"])]),_:2},1024)):u("",!0)]),_:2},1032,["modelValue","onUpdate:modelValue"]),a(b),d("div",M,[d("div",X,[y(a(m,{label:t.$t("label.cancel"),color:"red","no-caps":""},null,8,["label"]),[[_]])]),d("div",f,[y(a(m,{label:t.$t("label.ok"),color:"secondary","no-caps":"",onClick:l=>e.value=e.proxy_value},null,8,["label","onClick"]),[[_]])])])])]),_:2},1032,["onBeforeShow"])]),_:2},1032,["name"])]),_:2},1032,["label","modelValue","onUpdate:modelValue"]),a(p,{type:"text",label:e.label,modelValue:e.value2,"onUpdate:modelValue":l=>e.value2=l,filled:"",class:"col-6 q-pr-xs"},{append:o(()=>[a(U,{name:e.type==="time"?"schedule":e.type==="date"?"calendar_month":"event",class:"cursor-pointer"},{default:o(()=>[a(g,{"transition-show":"scale","transition-hide":"scale",cover:"",onBeforeShow:l=>v.uix.calendar.beforeShow(e,"tab2","proxy_value2","value2")},{default:o(()=>[d("div",ee,[a(w,{modelValue:e.tab2,"onUpdate:modelValue":l=>e.tab2=l,class:"bg-primary text-grey-5 shadow-2 text-subtitle2","no-caps":"","indicator-color":"transparent","active-color":"white"},{default:o(()=>[e.type==="datetime"||e.type==="date"?(s(),n(V,{key:0,name:"date"},{default:o(()=>[d("span",null,c(t.$t("label.date")),1)]),_:1})):u("",!0),e.type==="datetime"||e.type==="time"?(s(),n(V,{key:1,name:"time"},{default:o(()=>[d("span",null,c(t.$t("label.time")),1)]),_:1})):u("",!0)]),_:2},1032,["modelValue","onUpdate:modelValue"]),a(b),a(q,{modelValue:e.tab2,"onUpdate:modelValue":l=>e.tab2=l},{default:o(()=>[e.type==="datetime"||e.type==="date"?(s(),n(h,{key:0,name:"date",class:"q-pa-none"},{default:o(()=>[a(Q,{modelValue:e.proxy_value2,"onUpdate:modelValue":l=>e.proxy_value2=l,mask:e.pattern,square:""},null,8,["modelValue","onUpdate:modelValue","mask"])]),_:2},1024)):u("",!0),e.type==="datetime"||e.type==="time"?(s(),n(h,{key:1,name:"time",class:"q-pa-none"},{default:o(()=>[a(C,{modelValue:e.proxy_value2,"onUpdate:modelValue":l=>e.proxy_value2=l,mask:e.pattern,format24h:"",square:""},null,8,["modelValue","onUpdate:modelValue","mask"])]),_:2},1024)):u("",!0)]),_:2},1032,["modelValue","onUpdate:modelValue"]),a(b),d("div",ae,[d("div",le,[y(a(m,{label:t.$t("label.cancel"),color:"red","no-caps":""},null,8,["label"]),[[_]])]),d("div",oe,[y(a(m,{label:t.$t("label.ok"),color:"secondary","no-caps":"",onClick:l=>e.value2=e.proxy_value2},null,8,["label","onClick"]),[[_]])])])])]),_:2},1032,["onBeforeShow"])]),_:2},1032,["name"])]),_:2},1032,["label","modelValue","onUpdate:modelValue"])])):(s(),n(p,{key:1,type:"text",label:e.label,modelValue:e.value,"onUpdate:modelValue":l=>e.value=l,filled:""},{append:o(()=>[a(U,{name:e.type==="time"?"schedule":e.type==="date"?"calendar_month":"event",class:"cursor-pointer"},{default:o(()=>[a(g,{"transition-show":"scale","transition-hide":"scale",cover:"",onBeforeShow:l=>v.uix.calendar.beforeShow(e,"tab","proxy_value","value")},{default:o(()=>[d("div",te,[a(w,{modelValue:e.tab,"onUpdate:modelValue":l=>e.tab=l,class:"bg-primary text-grey-5 shadow-2 text-subtitle2","no-caps":"","indicator-color":"transparent","active-color":"white"},{default:o(()=>[e.type==="datetime"||e.type==="date"?(s(),n(V,{key:0,name:"date"},{default:o(()=>[d("span",null,c(t.$t("label.date")),1)]),_:1})):u("",!0),e.type==="datetime"||e.type==="time"?(s(),n(V,{key:1,name:"time"},{default:o(()=>[d("span",null,c(t.$t("label.time")),1)]),_:1})):u("",!0)]),_:2},1032,["modelValue","onUpdate:modelValue"]),a(b),a(q,{modelValue:e.tab,"onUpdate:modelValue":l=>e.tab=l},{default:o(()=>[e.type==="datetime"||e.type==="date"?(s(),n(h,{key:0,name:"date",class:"q-pa-none"},{default:o(()=>[a(Q,{modelValue:e.proxy_value,"onUpdate:modelValue":l=>e.proxy_value=l,mask:e.pattern,square:""},null,8,["modelValue","onUpdate:modelValue","mask"])]),_:2},1024)):u("",!0),e.type==="datetime"||e.type==="time"?(s(),n(h,{key:1,name:"time",class:"q-pa-none"},{default:o(()=>[a(C,{modelValue:e.proxy_value,"onUpdate:modelValue":l=>e.proxy_value=l,mask:e.pattern,format24h:"",square:""},null,8,["modelValue","onUpdate:modelValue","mask"])]),_:2},1024)):u("",!0)]),_:2},1032,["modelValue","onUpdate:modelValue"]),a(b),d("div",se,[d("div",de,[y(a(m,{label:t.$t("label.cancel"),color:"red","no-caps":""},null,8,["label"]),[[_]])]),d("div",ne,[y(a(m,{label:t.$t("label.ok"),color:"secondary","no-caps":"",onClick:l=>e.value=e.proxy_value},null,8,["label","onClick"]),[[_]])])])])]),_:2},1032,["onBeforeShow"])]),_:2},1032,["name"])]),_:2},1032,["label","modelValue","onUpdate:modelValue"]))])):(s(),r("div",ue,[e.condition==="BETWEEN"?(s(),r("div",me,[a(p,{type:"text",label:e.label,modelValue:e.value,"onUpdate:modelValue":l=>e.value=l,filled:"",class:"col-6 q-pr-xs"},null,8,["label","modelValue","onUpdate:modelValue"]),a(p,{type:"text",label:e.label,modelValue:e.value2,"onUpdate:modelValue":l=>e.value2=l,filled:"",class:"col-6 q-pl-xs"},null,8,["label","modelValue","onUpdate:modelValue"])])):e.type?(s(),n(p,{key:1,type:"text",label:e.label,modelValue:e.value,"onUpdate:modelValue":l=>e.value=l,filled:""},null,8,["label","modelValue","onUpdate:modelValue"])):u("",!0)]))]),_:2},1032,["onSubmit","onReset"])]))),128))]),_:1}),a(b),a(R,{class:"row"},{default:o(()=>[d("div",pe,[a(m,{label:t.$t("label.reset"),color:"orange","no-caps":"",glossy:"",onClick:k.on_reset_click},null,8,["label","onClick"])]),d("div",ce,[a(m,{label:t.$t("label.filter"),color:"purple","no-caps":"",glossy:"",onClick:k.on_filter_click},null,8,["label","onClick"])])]),_:1})]),_:1},8,["style"])}const Qe=D(K,[["render",re]]);export{Qe as default};
