import{_ as m,Y as _,Z as i,$ as f,a0 as r,r as u,a2 as k,a5 as o,a3 as p,a4 as P,f as a,a6 as n,a9 as h,a7 as g,a8 as b,ab as Q}from"./index-DR8vcSVV.js";import{Q as w}from"./QSpace-KGtmaO9N.js";import{Q as v}from"./QTooltip-yDWh9haU.js";import{e as y,a as C,b as q,c as z,d as A}from"./QLayout-CmI8kD7U.js";import"./QResizeObserver-BZLpK1Mj.js";let d;const x={setup(){return{APP:_,util:i,api:f,uix:r,is_dark_mode:u(!1)}},created(){d=this,d.is_dark_mode=r.dark.active()},methods:{on_header_menu_click(){window.location.href=i.webPath()+"/"},on_dark_click(){r.dark.toggle(),this.is_dark_mode=r.dark.active()}}};function B(l,t,S,e,T,s){const c=p("router-view");return P(),k(y,{view:"hHh lpR fFf",class:"background-layout"},{default:o(()=>[a(q,{class:"header-main",style:Q((e.APP?.color?.header?"background: "+e.APP.color.header+" !important;":"")+(e.APP?.color?.title?"color: "+e.APP.color.title+" !important;":""))},{default:o(()=>[a(C,null,{default:o(()=>[a(n,{flat:"","no-caps":"","no-wrap":"",label:e.APP.title,size:l.$q.screen.gt.sm?"xl":"lg",class:h("q-pa-xs text-weight-bold "+(l.$q.screen.gt.sm?"q-ml-md":"q-ml-xs")),onClick:t[0]||(t[0]=H=>s.on_header_menu_click())},null,8,["label","size","class"]),a(w),a(n,{round:"",icon:e.is_dark_mode?"light_mode":"dark_mode",size:l.$q.screen.gt.sm?"md":"sm",onClick:s.on_dark_click},{default:o(()=>[a(v,null,{default:o(()=>[g(b(e.is_dark_mode?l.$t("label.light"):l.$t("label.dark")),1)]),_:1})]),_:1},8,["icon","size","onClick"])]),_:1})]),_:1},8,["style"]),a(A,null,{default:o(()=>[a(z,null,{default:o(()=>[a(c)]),_:1})]),_:1})]),_:1})}const F=m(x,[["render",B]]);export{F as default};
