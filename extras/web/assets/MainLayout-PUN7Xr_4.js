import{Q as pe}from"./QTooltip-DDLN5EO4.js";import{l as ce,n as _e,h as y,p as Ge,q as mt,u as Xe,t as Je,v as Qe,x as Ze,y as Me,r as T,z as gt,A as et,c as u,w as O,g as Se,B as bt,Q as be,C as ie,D as yt,E as $e,G as te,H as _t,I as St,J as kt,K as wt,L as zt,M as Oe,N as Le,O as xt,i as tt,P as se,R as qt,o as Tt,S as je,U as Ve,V as at,W as Ct,X as pt,_ as Ot,Y as It,Z as Q,$ as Ie,a0 as de,a1 as j,a2 as B,a3 as Pt,a4 as p,a5 as b,f as z,a6 as fe,a7 as K,a8 as G,a9 as W,aa as ve,ab as Bt,ac as ue,ad as Pe,F as we,ae as he,af as Be}from"./index-C3Shutt2.js";import{Q as At}from"./QSpace-DddE7BY7.js";import{Q as Lt,a as Qt,b as Mt,c as $t,d as Ht,e as Dt}from"./QLayout-BDp5rG1W.js";import{Q as ge,a as Re,b as M,c as X}from"./format-RPhhBnVv.js";import{Q as jt,a as me}from"./QList-BLyCLZgO.js";import{Q as Ee}from"./QResizeObserver-Dswlw4eJ.js";import{T as ye}from"./TouchPan-C7TgOV9p.js";const Vt=ce({name:"QSlideTransition",props:{appear:Boolean,duration:{type:Number,default:300}},emits:["show","hide"],setup(e,{slots:m,emit:S}){let i=!1,f,r,n=null,t=null,q,x;function l(){f&&f(),f=null,i=!1,n!==null&&(clearTimeout(n),n=null),t!==null&&(clearTimeout(t),t=null),r!==void 0&&r.removeEventListener("transitionend",q),q=null}function v(s,k,h){k!==void 0&&(s.style.height=`${k}px`),s.style.transition=`height ${e.duration}ms cubic-bezier(.25, .8, .50, 1)`,i=!0,f=h}function g(s,k){s.style.overflowY=null,s.style.height=null,s.style.transition=null,l(),k!==x&&S(k)}function I(s,k){let h=0;r=s,i===!0?(l(),h=s.offsetHeight===s.scrollHeight?0:void 0):(x="hide",s.style.overflowY="hidden"),v(s,h,k),n=setTimeout(()=>{n=null,s.style.height=`${s.scrollHeight}px`,q=$=>{t=null,(Object($)!==$||$.target===s)&&g(s,"show")},s.addEventListener("transitionend",q),t=setTimeout(q,e.duration*1.1)},100)}function C(s,k){let h;r=s,i===!0?l():(x="show",s.style.overflowY="hidden",h=s.scrollHeight),v(s,h,k),n=setTimeout(()=>{n=null,s.style.height=0,q=$=>{t=null,(Object($)!==$||$.target===s)&&g(s,"hide")},s.addEventListener("transitionend",q),t=setTimeout(q,e.duration*1.1)},100)}return _e(()=>{i===!0&&l()}),()=>y(Ge,{css:!1,appear:e.appear,onEnter:I,onLeave:C},m.default)}}),ne=mt({}),Rt=Object.keys(Xe),Ne=ce({name:"QExpansionItem",props:{...Xe,...Je,...Qe,icon:String,label:String,labelLines:[Number,String],caption:String,captionLines:[Number,String],dense:Boolean,toggleAriaLabel:String,expandIcon:String,expandedIcon:String,expandIconClass:[Array,String,Object],duration:{},headerInsetLevel:Number,contentInsetLevel:Number,expandSeparator:Boolean,defaultOpened:Boolean,hideExpandIcon:Boolean,expandIconToggle:Boolean,switchToggleSide:Boolean,denseToggle:Boolean,group:String,popup:Boolean,headerStyle:[Array,String,Object],headerClass:[Array,String,Object]},emits:[...Ze,"click","afterShow","afterHide"],setup(e,{slots:m,emit:S}){const{proxy:{$q:i}}=Se(),f=Me(e,i),r=T(e.modelValue!==null?e.modelValue:e.defaultOpened),n=T(null),t=gt(),{show:q,hide:x,toggle:l}=et({showing:r});let v,g;const I=u(()=>`q-expansion-item q-item-type q-expansion-item--${r.value===!0?"expanded":"collapsed"} q-expansion-item--${e.popup===!0?"popup":"standard"}`),C=u(()=>e.contentInsetLevel===void 0?null:{["padding"+(i.lang.rtl===!0?"Right":"Left")]:e.contentInsetLevel*56+"px"}),s=u(()=>e.disable!==!0&&(e.href!==void 0||e.to!==void 0&&e.to!==null&&e.to!=="")),k=u(()=>{const d={};return Rt.forEach(P=>{d[P]=e[P]}),d}),h=u(()=>s.value===!0||e.expandIconToggle!==!0),$=u(()=>e.expandedIcon!==void 0&&r.value===!0?e.expandedIcon:e.expandIcon||i.iconSet.expansionItem[e.denseToggle===!0?"denseIcon":"icon"]),oe=u(()=>e.disable!==!0&&(s.value===!0||e.expandIconToggle===!0)),re=u(()=>({expanded:r.value===!0,detailsId:t.value,toggle:l,show:q,hide:x})),U=u(()=>{const d=e.toggleAriaLabel!==void 0?e.toggleAriaLabel:i.lang.label[r.value===!0?"collapse":"expand"](e.label);return{role:"button","aria-expanded":r.value===!0?"true":"false","aria-controls":t.value,"aria-label":d}});O(()=>e.group,d=>{g!==void 0&&g(),d!==void 0&&E()});function N(d){s.value!==!0&&l(d),S("click",d)}function J(d){d.keyCode===13&&ae(d,!0)}function ae(d,P){P!==!0&&n.value!==null&&n.value.focus(),l(d),_t(d)}function H(){S("afterShow")}function V(){S("afterHide")}function E(){v===void 0&&(v=bt()),r.value===!0&&(ne[e.group]=v);const d=O(r,L=>{L===!0?ne[e.group]=v:ne[e.group]===v&&delete ne[e.group]}),P=O(()=>ne[e.group],(L,le)=>{le===v&&L!==void 0&&L!==v&&x()});g=()=>{d(),P(),ne[e.group]===v&&delete ne[e.group],g=void 0}}function Y(){const d={class:[`q-focusable relative-position cursor-pointer${e.denseToggle===!0&&e.switchToggleSide===!0?" items-end":""}`,e.expandIconClass],side:e.switchToggleSide!==!0,avatar:e.switchToggleSide},P=[y(te,{class:"q-expansion-item__toggle-icon"+(e.expandedIcon===void 0&&r.value===!0?" q-expansion-item__toggle-icon--rotated":""),name:$.value})];return oe.value===!0&&(Object.assign(d,{tabindex:0,...U.value,onClick:ae,onKeyup:J}),P.unshift(y("div",{ref:n,class:"q-expansion-item__toggle-focus q-icon q-focus-helper q-focus-helper--rounded",tabindex:-1}))),y(M,d,()=>P)}function F(){let d;return m.header!==void 0?d=[].concat(m.header(re.value)):(d=[y(M,()=>[y(Re,{lines:e.labelLines},()=>e.label||""),e.caption?y(Re,{lines:e.captionLines,caption:!0},()=>e.caption):null])],e.icon&&d[e.switchToggleSide===!0?"push":"unshift"](y(M,{side:e.switchToggleSide===!0,avatar:e.switchToggleSide!==!0},()=>y(te,{name:e.icon})))),e.disable!==!0&&e.hideExpandIcon!==!0&&d[e.switchToggleSide===!0?"unshift":"push"](Y()),d}function o(){const d={ref:"item",style:e.headerStyle,class:e.headerClass,dark:f.value,disable:e.disable,dense:e.dense,insetLevel:e.headerInsetLevel};return h.value===!0&&(d.clickable=!0,d.onClick=N,Object.assign(d,s.value===!0?k.value:U.value)),y(ge,d,F)}function c(){return ie(y("div",{key:"e-content",class:"q-expansion-item__content relative-position",style:C.value,id:t.value},$e(m.default)),[[yt,r.value]])}function w(){const d=[o(),y(Vt,{duration:e.duration,onShow:H,onHide:V},c)];return e.expandSeparator===!0&&d.push(y(be,{class:"q-expansion-item__border q-expansion-item__border--top absolute-top",dark:f.value}),y(be,{class:"q-expansion-item__border q-expansion-item__border--bottom absolute-bottom",dark:f.value})),d}return e.group!==void 0&&E(),_e(()=>{g!==void 0&&g()}),()=>y("div",{class:I.value},[y("div",{class:"q-expansion-item__container relative-position"},w())])}}),Et=ce({props:["store","barStyle","verticalBarStyle","horizontalBarStyle"],setup(e){return()=>[y("div",{class:e.store.scroll.vertical.barClass.value,style:[e.barStyle,e.verticalBarStyle],"aria-hidden":"true",onMousedown:e.store.onVerticalMousedown}),y("div",{class:e.store.scroll.horizontal.barClass.value,style:[e.barStyle,e.horizontalBarStyle],"aria-hidden":"true",onMousedown:e.store.onHorizontalMousedown}),ie(y("div",{ref:e.store.scroll.vertical.ref,class:e.store.scroll.vertical.thumbClass.value,style:e.store.scroll.vertical.style.value,"aria-hidden":"true"}),e.store.thumbVertDir),ie(y("div",{ref:e.store.scroll.horizontal.ref,class:e.store.scroll.horizontal.thumbClass.value,style:e.store.scroll.horizontal.style.value,"aria-hidden":"true"}),e.store.thumbHorizDir)]}}),Fe=["vertical","horizontal"],Ae={vertical:{offset:"offsetY",scroll:"scrollTop",dir:"down",dist:"y"},horizontal:{offset:"offsetX",scroll:"scrollLeft",dir:"right",dist:"x"}},We={prevent:!0,mouse:!0,mouseAllDir:!0},Ue=e=>e>=250?50:Math.ceil(e/5),Nt=ce({name:"QScrollArea",props:{...Qe,thumbStyle:Object,verticalThumbStyle:Object,horizontalThumbStyle:Object,barStyle:[Array,String,Object],verticalBarStyle:[Array,String,Object],horizontalBarStyle:[Array,String,Object],verticalOffset:{type:Array,default:[0,0]},horizontalOffset:{type:Array,default:[0,0]},contentStyle:[Array,String,Object],contentActiveStyle:[Array,String,Object],delay:{type:[String,Number],default:1e3},visible:{type:Boolean,default:null},tabindex:[String,Number],onScroll:Function},setup(e,{slots:m,emit:S}){const i=T(!1),f=T(!1),r=T(!1),n={vertical:T(0),horizontal:T(0)},t={vertical:{ref:T(null),position:T(0),size:T(0)},horizontal:{ref:T(null),position:T(0),size:T(0)}},{proxy:q}=Se(),x=Me(e,q.$q);let l=null,v;const g=T(null),I=u(()=>"q-scrollarea"+(x.value===!0?" q-scrollarea--dark":""));Object.assign(n,{verticalInner:u(()=>n.vertical.value-e.verticalOffset[0]-e.verticalOffset[1]),horizontalInner:u(()=>n.horizontal.value-e.horizontalOffset[0]-e.horizontalOffset[1])}),t.vertical.percentage=u(()=>{const o=t.vertical.size.value-n.vertical.value;if(o<=0)return 0;const c=X(t.vertical.position.value/o,0,1);return Math.round(c*1e4)/1e4}),t.vertical.thumbHidden=u(()=>(e.visible===null?r.value:e.visible)!==!0&&i.value===!1&&f.value===!1||t.vertical.size.value<=n.vertical.value+1),t.vertical.thumbStart=u(()=>e.verticalOffset[0]+t.vertical.percentage.value*(n.verticalInner.value-t.vertical.thumbSize.value)),t.vertical.thumbSize=u(()=>Math.round(X(n.verticalInner.value*n.verticalInner.value/t.vertical.size.value,Ue(n.verticalInner.value),n.verticalInner.value))),t.vertical.style=u(()=>({...e.thumbStyle,...e.verticalThumbStyle,top:`${t.vertical.thumbStart.value}px`,height:`${t.vertical.thumbSize.value}px`,right:`${e.horizontalOffset[1]}px`})),t.vertical.thumbClass=u(()=>"q-scrollarea__thumb q-scrollarea__thumb--v absolute-right"+(t.vertical.thumbHidden.value===!0?" q-scrollarea__thumb--invisible":"")),t.vertical.barClass=u(()=>"q-scrollarea__bar q-scrollarea__bar--v absolute-right"+(t.vertical.thumbHidden.value===!0?" q-scrollarea__bar--invisible":"")),t.horizontal.percentage=u(()=>{const o=t.horizontal.size.value-n.horizontal.value;if(o<=0)return 0;const c=X(Math.abs(t.horizontal.position.value)/o,0,1);return Math.round(c*1e4)/1e4}),t.horizontal.thumbHidden=u(()=>(e.visible===null?r.value:e.visible)!==!0&&i.value===!1&&f.value===!1||t.horizontal.size.value<=n.horizontal.value+1),t.horizontal.thumbStart=u(()=>e.horizontalOffset[0]+t.horizontal.percentage.value*(n.horizontalInner.value-t.horizontal.thumbSize.value)),t.horizontal.thumbSize=u(()=>Math.round(X(n.horizontalInner.value*n.horizontalInner.value/t.horizontal.size.value,Ue(n.horizontalInner.value),n.horizontalInner.value))),t.horizontal.style=u(()=>({...e.thumbStyle,...e.horizontalThumbStyle,[q.$q.lang.rtl===!0?"right":"left"]:`${t.horizontal.thumbStart.value}px`,width:`${t.horizontal.thumbSize.value}px`,bottom:`${e.verticalOffset[1]}px`})),t.horizontal.thumbClass=u(()=>"q-scrollarea__thumb q-scrollarea__thumb--h absolute-bottom"+(t.horizontal.thumbHidden.value===!0?" q-scrollarea__thumb--invisible":"")),t.horizontal.barClass=u(()=>"q-scrollarea__bar q-scrollarea__bar--h absolute-bottom"+(t.horizontal.thumbHidden.value===!0?" q-scrollarea__bar--invisible":""));const C=u(()=>t.vertical.thumbHidden.value===!0&&t.horizontal.thumbHidden.value===!0?e.contentStyle:e.contentActiveStyle);function s(){const o={};return Fe.forEach(c=>{const w=t[c];Object.assign(o,{[c+"Position"]:w.position.value,[c+"Percentage"]:w.percentage.value,[c+"Size"]:w.size.value,[c+"ContainerSize"]:n[c].value,[c+"ContainerInnerSize"]:n[c+"Inner"].value})}),o}const k=St(()=>{const o=s();o.ref=q,S("scroll",o)},0);function h(o,c,w){if(Fe.includes(o)===!1){console.error("[QScrollArea]: wrong first param of setScrollPosition (vertical/horizontal)");return}(o==="vertical"?Le:Oe)(g.value,c,w)}function $({height:o,width:c}){let w=!1;n.vertical.value!==o&&(n.vertical.value=o,w=!0),n.horizontal.value!==c&&(n.horizontal.value=c,w=!0),w===!0&&J()}function oe({position:o}){let c=!1;t.vertical.position.value!==o.top&&(t.vertical.position.value=o.top,c=!0),t.horizontal.position.value!==o.left&&(t.horizontal.position.value=o.left,c=!0),c===!0&&J()}function re({height:o,width:c}){t.horizontal.size.value!==c&&(t.horizontal.size.value=c,J()),t.vertical.size.value!==o&&(t.vertical.size.value=o,J())}function U(o,c){const w=t[c];if(o.isFirst===!0){if(w.thumbHidden.value===!0)return;v=w.position.value,f.value=!0}else if(f.value!==!0)return;o.isFinal===!0&&(f.value=!1);const d=Ae[c],P=(w.size.value-n[c].value)/(n[c+"Inner"].value-w.thumbSize.value),L=o.distance[d.dist],le=v+(o.direction===d.dir?1:-1)*L*P;ae(le,c)}function N(o,c){const w=t[c];if(w.thumbHidden.value!==!0){const d=c==="vertical"?e.verticalOffset[0]:e.horizontalOffset[0],P=o[Ae[c].offset]-d,L=w.thumbStart.value-d;if(P<L||P>L+w.thumbSize.value){const le=P-w.thumbSize.value/2,ze=X(le/(n[c+"Inner"].value-w.thumbSize.value),0,1);ae(ze*Math.max(0,w.size.value-n[c].value),c)}w.ref.value!==null&&w.ref.value.dispatchEvent(new MouseEvent(o.type,o))}}function J(){i.value=!0,l!==null&&clearTimeout(l),l=setTimeout(()=>{l=null,i.value=!1},e.delay),e.onScroll!==void 0&&k()}function ae(o,c){g.value[Ae[c].scroll]=o}let H=null;function V(){H!==null&&clearTimeout(H),H=setTimeout(()=>{H=null,r.value=!0},q.$q.platform.is.ios?50:0)}function E(){H!==null&&(clearTimeout(H),H=null),r.value=!1}let Y=null;O(()=>q.$q.lang.rtl,o=>{g.value!==null&&Oe(g.value,Math.abs(t.horizontal.position.value)*(o===!0?-1:1))}),kt(()=>{Y={top:t.vertical.position.value,left:t.horizontal.position.value}}),wt(()=>{if(Y===null)return;const o=g.value;o!==null&&(Oe(o,Y.left),Le(o,Y.top))}),_e(k.cancel),Object.assign(q,{getScrollTarget:()=>g.value,getScroll:s,getScrollPosition:()=>({top:t.vertical.position.value,left:t.horizontal.position.value}),getScrollPercentage:()=>({top:t.vertical.percentage.value,left:t.horizontal.percentage.value}),setScrollPosition:h,setScrollPercentage(o,c,w){h(o,c*(t[o].size.value-n[o].value)*(o==="horizontal"&&q.$q.lang.rtl===!0?-1:1),w)}});const F={scroll:t,thumbVertDir:[[ye,o=>{U(o,"vertical")},void 0,{vertical:!0,...We}]],thumbHorizDir:[[ye,o=>{U(o,"horizontal")},void 0,{horizontal:!0,...We}]],onVerticalMousedown(o){N(o,"vertical")},onHorizontalMousedown(o){N(o,"horizontal")}};return()=>y("div",{class:I.value,onMouseenter:V,onMouseleave:E},[y("div",{ref:g,class:"q-scrollarea__container scroll relative-position fit hide-scrollbar",tabindex:e.tabindex!==void 0?e.tabindex:void 0},[y("div",{class:"q-scrollarea__content absolute",style:C.value},zt(m.default,[y(Ee,{debounce:0,onResize:re})])),y(Lt,{axis:"both",onScroll:oe})]),y(Ee,{debounce:0,onResize:$}),y(Et,{store:F,barStyle:e.barStyle,verticalBarStyle:e.verticalBarStyle,horizontalBarStyle:e.horizontalBarStyle})])}}),Ye=150,Ft=ce({name:"QDrawer",inheritAttrs:!1,props:{...Je,...Qe,side:{type:String,default:"left",validator:e=>["left","right"].includes(e)},width:{type:Number,default:300},mini:Boolean,miniToOverlay:Boolean,miniWidth:{type:Number,default:57},noMiniAnimation:Boolean,breakpoint:{type:Number,default:1023},showIfAbove:Boolean,behavior:{type:String,validator:e=>["default","desktop","mobile"].includes(e),default:"default"},bordered:Boolean,elevated:Boolean,overlay:Boolean,persistent:Boolean,noSwipeOpen:Boolean,noSwipeClose:Boolean,noSwipeBackdrop:Boolean},emits:[...Ze,"onLayout","miniState"],setup(e,{slots:m,emit:S,attrs:i}){const f=Se(),{proxy:{$q:r}}=f,n=Me(e,r),{preventBodyScroll:t}=Ct(),{registerTimeout:q,removeTimeout:x}=xt(),l=tt(at,se);if(l===se)return console.error("QDrawer needs to be child of QLayout"),se;let v,g=null,I;const C=T(e.behavior==="mobile"||e.behavior!=="desktop"&&l.totalWidth.value<=e.breakpoint),s=u(()=>e.mini===!0&&C.value!==!0),k=u(()=>s.value===!0?e.miniWidth:e.width),h=T(e.showIfAbove===!0&&C.value===!1?!0:e.modelValue===!0),$=u(()=>e.persistent!==!0&&(C.value===!0||le.value===!0));function oe(a,_){if(J(),a!==!1&&l.animate(),R(0),C.value===!0){const D=l.instances[w.value];D!==void 0&&D.belowBreakpoint===!0&&D.hide(!1),Z(1),l.isContainer.value!==!0&&t(!0)}else Z(0),a!==!1&&qe(!1);q(()=>{a!==!1&&qe(!0),_!==!0&&S("show",a)},Ye)}function re(a,_){ae(),a!==!1&&l.animate(),Z(0),R(E.value*k.value),Te(),_!==!0?q(()=>{S("hide",a)},Ye):x()}const{show:U,hide:N}=et({showing:h,hideOnRouteChange:$,handleShow:oe,handleHide:re}),{addToHistory:J,removeFromHistory:ae}=qt(h,N,$),H={belowBreakpoint:C,hide:N},V=u(()=>e.side==="right"),E=u(()=>(r.lang.rtl===!0?-1:1)*(V.value===!0?1:-1)),Y=T(0),F=T(!1),o=T(!1),c=T(k.value*E.value),w=u(()=>V.value===!0?"left":"right"),d=u(()=>h.value===!0&&C.value===!1&&e.overlay===!1?e.miniToOverlay===!0?e.miniWidth:k.value:0),P=u(()=>e.overlay===!0||e.miniToOverlay===!0||l.view.value.indexOf(V.value?"R":"L")!==-1||r.platform.is.ios===!0&&l.isContainer.value===!0),L=u(()=>e.overlay===!1&&h.value===!0&&C.value===!1),le=u(()=>e.overlay===!0&&h.value===!0&&C.value===!1),ze=u(()=>"fullscreen q-drawer__backdrop"+(h.value===!1&&F.value===!1?" hidden":"")),lt=u(()=>({backgroundColor:`rgba(0,0,0,${Y.value*.4})`})),He=u(()=>V.value===!0?l.rows.value.top[2]==="r":l.rows.value.top[0]==="l"),nt=u(()=>V.value===!0?l.rows.value.bottom[2]==="r":l.rows.value.bottom[0]==="l"),it=u(()=>{const a={};return l.header.space===!0&&He.value===!1&&(P.value===!0?a.top=`${l.header.offset}px`:l.header.space===!0&&(a.top=`${l.header.size}px`)),l.footer.space===!0&&nt.value===!1&&(P.value===!0?a.bottom=`${l.footer.offset}px`:l.footer.space===!0&&(a.bottom=`${l.footer.size}px`)),a}),ot=u(()=>{const a={width:`${k.value}px`,transform:`translateX(${c.value}px)`};return C.value===!0?a:Object.assign(a,it.value)}),rt=u(()=>"q-drawer__content fit "+(l.isContainer.value!==!0?"scroll":"overflow-auto")),ut=u(()=>`q-drawer q-drawer--${e.side}`+(o.value===!0?" q-drawer--mini-animate":"")+(e.bordered===!0?" q-drawer--bordered":"")+(n.value===!0?" q-drawer--dark q-dark":"")+(F.value===!0?" no-transition":h.value===!0?"":" q-layout--prevent-focus")+(C.value===!0?" fixed q-drawer--on-top q-drawer--mobile q-drawer--top-padding":` q-drawer--${s.value===!0?"mini":"standard"}`+(P.value===!0||L.value!==!0?" fixed":"")+(e.overlay===!0||e.miniToOverlay===!0?" q-drawer--on-top":"")+(He.value===!0?" q-drawer--top-padding":""))),st=u(()=>{const a=r.lang.rtl===!0?e.side:w.value;return[[ye,vt,void 0,{[a]:!0,mouse:!0}]]}),ct=u(()=>{const a=r.lang.rtl===!0?w.value:e.side;return[[ye,De,void 0,{[a]:!0,mouse:!0}]]}),dt=u(()=>{const a=r.lang.rtl===!0?w.value:e.side;return[[ye,De,void 0,{[a]:!0,mouse:!0,mouseAllDir:!0}]]});function xe(){ht(C,e.behavior==="mobile"||e.behavior!=="desktop"&&l.totalWidth.value<=e.breakpoint)}O(C,a=>{a===!0?(v=h.value,h.value===!0&&N(!1)):e.overlay===!1&&e.behavior!=="mobile"&&v!==!1&&(h.value===!0?(R(0),Z(0),Te()):U(!1))}),O(()=>e.side,(a,_)=>{l.instances[_]===H&&(l.instances[_]=void 0,l[_].space=!1,l[_].offset=0),l.instances[a]=H,l[a].size=k.value,l[a].space=L.value,l[a].offset=d.value}),O(l.totalWidth,()=>{(l.isContainer.value===!0||document.qScrollPrevented!==!0)&&xe()}),O(()=>e.behavior+e.breakpoint,xe),O(l.isContainer,a=>{h.value===!0&&t(a!==!0),a===!0&&xe()}),O(l.scrollbarWidth,()=>{R(h.value===!0?0:void 0)}),O(d,a=>{ee("offset",a)}),O(L,a=>{S("onLayout",a),ee("space",a)}),O(V,()=>{R()}),O(k,a=>{R(),Ce(e.miniToOverlay,a)}),O(()=>e.miniToOverlay,a=>{Ce(a,k.value)}),O(()=>r.lang.rtl,()=>{R()}),O(()=>e.mini,()=>{e.noMiniAnimation||e.modelValue===!0&&(ft(),l.animate())}),O(s,a=>{S("miniState",a)});function R(a){a===void 0?je(()=>{a=h.value===!0?0:k.value,R(E.value*a)}):(l.isContainer.value===!0&&V.value===!0&&(C.value===!0||Math.abs(a)===k.value)&&(a+=E.value*l.scrollbarWidth.value),c.value=a)}function Z(a){Y.value=a}function qe(a){const _=a===!0?"remove":l.isContainer.value!==!0?"add":"";_!==""&&document.body.classList[_]("q-body--drawer-toggle")}function ft(){g!==null&&clearTimeout(g),f.proxy&&f.proxy.$el&&f.proxy.$el.classList.add("q-drawer--mini-animate"),o.value=!0,g=setTimeout(()=>{g=null,o.value=!1,f&&f.proxy&&f.proxy.$el&&f.proxy.$el.classList.remove("q-drawer--mini-animate")},150)}function vt(a){if(h.value!==!1)return;const _=k.value,D=X(a.distance.x,0,_);if(a.isFinal===!0){D>=Math.min(75,_)===!0?U():(l.animate(),Z(0),R(E.value*_)),F.value=!1;return}R((r.lang.rtl===!0?V.value!==!0:V.value)?Math.max(_-D,0):Math.min(0,D-_)),Z(X(D/_,0,1)),a.isFirst===!0&&(F.value=!0)}function De(a){if(h.value!==!0)return;const _=k.value,D=a.direction===e.side,ke=(r.lang.rtl===!0?D!==!0:D)?X(a.distance.x,0,_):0;if(a.isFinal===!0){Math.abs(ke)<Math.min(75,_)===!0?(l.animate(),Z(1),R(0)):N(),F.value=!1;return}R(E.value*ke),Z(X(1-ke/_,0,1)),a.isFirst===!0&&(F.value=!0)}function Te(){t(!1),qe(!0)}function ee(a,_){l.update(e.side,a,_)}function ht(a,_){a.value!==_&&(a.value=_)}function Ce(a,_){ee("size",a===!0?e.miniWidth:_)}return l.instances[e.side]=H,Ce(e.miniToOverlay,k.value),ee("space",L.value),ee("offset",d.value),e.showIfAbove===!0&&e.modelValue!==!0&&h.value===!0&&e["onUpdate:modelValue"]!==void 0&&S("update:modelValue",!0),Tt(()=>{S("onLayout",L.value),S("miniState",s.value),v=e.showIfAbove===!0;const a=()=>{(h.value===!0?oe:re)(!1,!0)};if(l.totalWidth.value!==0){je(a);return}I=O(l.totalWidth,()=>{I(),I=void 0,h.value===!1&&e.showIfAbove===!0&&C.value===!1?U(!1):a()})}),_e(()=>{I!==void 0&&I(),g!==null&&(clearTimeout(g),g=null),h.value===!0&&Te(),l.instances[e.side]===H&&(l.instances[e.side]=void 0,ee("size",0),ee("offset",0),ee("space",!1))}),()=>{const a=[];C.value===!0&&(e.noSwipeOpen===!1&&a.push(ie(y("div",{key:"open",class:`q-drawer__opener fixed-${e.side}`,"aria-hidden":"true"}),st.value)),a.push(Ve("div",{ref:"backdrop",class:ze.value,style:lt.value,"aria-hidden":"true",onClick:N},void 0,"backdrop",e.noSwipeBackdrop!==!0&&h.value===!0,()=>dt.value)));const _=s.value===!0&&m.mini!==void 0,D=[y("div",{...i,key:""+_,class:[rt.value,i.class]},_===!0?m.mini():$e(m.default))];return e.elevated===!0&&h.value===!0&&D.push(y("div",{class:"q-layout__shadow absolute-full overflow-hidden no-pointer-events"})),a.push(Ve("aside",{ref:"content",class:ut.value,style:ot.value},D,"contentclose",e.noSwipeClose!==!0&&C.value===!0,()=>ct.value)),y("div",{class:"q-drawer-container"},a)}}}),Ke={position:{type:String,default:"bottom-right",validator:e=>["top-right","top-left","bottom-right","bottom-left","top","right","bottom","left"].includes(e)},offset:{type:Array,validator:e=>e.length===2},expand:Boolean};function Wt(){const{props:e,proxy:{$q:m}}=Se(),S=tt(at,se);if(S===se)return console.error("QPageSticky needs to be child of QLayout"),se;const i=u(()=>{const v=e.position;return{top:v.indexOf("top")!==-1,right:v.indexOf("right")!==-1,bottom:v.indexOf("bottom")!==-1,left:v.indexOf("left")!==-1,vertical:v==="top"||v==="bottom",horizontal:v==="left"||v==="right"}}),f=u(()=>S.header.offset),r=u(()=>S.right.offset),n=u(()=>S.footer.offset),t=u(()=>S.left.offset),q=u(()=>{let v=0,g=0;const I=i.value,C=m.lang.rtl===!0?-1:1;I.top===!0&&f.value!==0?g=`${f.value}px`:I.bottom===!0&&n.value!==0&&(g=`${-n.value}px`),I.left===!0&&t.value!==0?v=`${C*t.value}px`:I.right===!0&&r.value!==0&&(v=`${-C*r.value}px`);const s={transform:`translate(${v}, ${g})`};return e.offset&&(s.margin=`${e.offset[1]}px ${e.offset[0]}px`),I.vertical===!0?(t.value!==0&&(s[m.lang.rtl===!0?"right":"left"]=`${t.value}px`),r.value!==0&&(s[m.lang.rtl===!0?"left":"right"]=`${r.value}px`)):I.horizontal===!0&&(f.value!==0&&(s.top=`${f.value}px`),n.value!==0&&(s.bottom=`${n.value}px`)),s}),x=u(()=>`q-page-sticky row flex-center fixed-${e.position} q-page-sticky--${e.expand===!0?"expand":"shrink"}`);function l(v){const g=$e(v.default);return y("div",{class:x.value,style:q.value},e.expand===!0?g:[y("div",g)])}return{$layout:S,getStickyContent:l}}const Ut=ce({name:"QPageScroller",props:{...Ke,scrollOffset:{type:Number,default:1e3},reverse:Boolean,duration:{type:Number,default:300},offset:{...Ke.offset,default:()=>[18,18]}},emits:["click"],setup(e,{slots:m,emit:S}){const{proxy:{$q:i}}=Se(),{$layout:f,getStickyContent:r}=Wt(),n=T(null);let t;const q=u(()=>f.height.value-(f.isContainer.value===!0?f.containerHeight.value:i.screen.height));function x(){return e.reverse===!0?q.value-f.scroll.value.position>e.scrollOffset:f.scroll.value.position>e.scrollOffset}const l=T(x());function v(){const k=x();l.value!==k&&(l.value=k)}function g(){e.reverse===!0?t===void 0&&(t=O(q,v)):I()}O(f.scroll,v),O(()=>e.reverse,g);function I(){t!==void 0&&(t(),t=void 0)}function C(k){const h=pt(f.isContainer.value===!0?n.value:f.rootRef.value);Le(h,e.reverse===!0?f.height.value:0,e.duration),S("click",k)}function s(){return l.value===!0?y("div",{ref:n,class:"q-page-scroller",onClick:C},r(m)):null}return g(),_e(I),()=>y(Ge,{name:"q-transition--fade"},s)}});let A;const Yt={setup(){return{APP:It,util:Q,api:Ie,uix:de,is_logged_in:T(!1),is_logout_progress:T(!1),is_show_menu:T(!1),is_dark_mode:T(!1),active_menu:T({id:{}}),menus:T([])}},created(){A=this;let e=j.auth();A.is_dark_mode=de.dark.active(),A.is_logged_in=Q.isString(e.token)&&e.token!=="",Ie.call({path:"/menus",onSuccess(m){let S=j.menu(),i,f=Q.isString(S.next)?S.next:"";m=Q.isArray(m)?m:[];for(const r of m){if(r.children?.length){r.icon=Q.isString(r.icon)&&r.icon!==""?r.icon:"dashboard";for(const n of r.children)if(n.children?.length){n.icon=Q.isString(n.icon)&&n.icon!==""?n.icon:"category";for(const t of n.children)t.icon=Q.isString(t.icon)&&t.icon!==""?t.icon:"stream",i=!i&&f===t.link?t:i}else n.icon=Q.isString(n.icon)&&n.icon!==""?n.icon:"blur_on",i=!i&&f===n.link?n:i}else r.icon=Q.isString(r.icon)&&r.icon!==""?r.icon:"extension",i=!i&&f===r.link?r:i;A.menus.push(r),A.menus.push({separator:!0})}A.active_menu=Q.isObject(i)?i:{id:{}},A.is_show_menu=S.show===!0,S.active=A.active_menu,delete S.next,j.menu(S)}})},methods:{on_toggle_menu:function(){A.is_show_menu=!A.is_show_menu;let e=j.menu();e.show=A.is_show_menu,j.menu(e)},on_header_menu_click(){A.active_menu={id:{}};let e=j.menu();delete e.active,j.menu(e),window.location.href=Q.webPath()+"/"},on_menu_click(e){if(Q.isString(e.link)&&e.link!=="")if(e.noPushRoute){A.active_menu={id:{}};let m=j.menu();delete m.active,j.menu(m),window.location.href=Q.webPath()+e.link}else{A.active_menu=e;let m=j.menu();m.active=e,j.menu(m),A.$router.push({path:e.link,query:Object.fromEntries([...new URLSearchParams(e.link.split("?")[1])])})}},on_logout_click(){de.confirm(function(){A.is_logout_progress=!0,j.auth().persistent===!0?(j.auth(null),window.location.href=Q.webPath(),A.is_logout_progress=!1):Ie.send({path:"/logout",method:"post",onFinish(){A.is_logout_progress=!1},onSuccess(){j.auth(null),window.location.href=Q.webPath()}})},"confirm.logout")},on_dark_click(){de.dark.toggle(),this.is_dark_mode=de.dark.active()}}};function Kt(e,m,S,i,f,r){const n=Pt("router-view");return p(),B(Dt,{view:"hHh lpR fFf",class:"background-layout"},{default:b(()=>[z(Mt,{class:"header-main",style:Bt((i.APP?.color?.header?"background: "+i.APP.color.header+" !important;":"")+(i.APP?.color?.title?"color: "+i.APP.color.title+" !important;":""))},{default:b(()=>[z(Qt,null,{default:b(()=>[i.menus?.length?(p(),B(fe,{key:0,round:"",size:e.$q.screen.gt.sm?"md":"sm","aria-label":i.APP.title,icon:"menu",onClick:r.on_toggle_menu},{default:b(()=>[z(pe,null,{default:b(()=>[K(G(e.$t("label.menu")),1)]),_:1})]),_:1},8,["size","aria-label","onClick"])):W("",!0),z(fe,{flat:"","no-caps":"","no-wrap":"",label:i.APP.title,size:e.$q.screen.gt.sm?"xl":"lg",class:ve("q-pa-xs text-weight-bold "+(e.$q.screen.gt.sm?"q-ml-md":"q-ml-xs")),onClick:m[0]||(m[0]=t=>r.on_header_menu_click())},null,8,["label","size","class"]),z(At),z(fe,{round:"",icon:i.is_dark_mode?"light_mode":"dark_mode",size:e.$q.screen.gt.sm?"md":"sm",onClick:r.on_dark_click},{default:b(()=>[z(pe,null,{default:b(()=>[K(G(i.is_dark_mode?e.$t("label.light"):e.$t("label.dark")),1)]),_:1})]),_:1},8,["icon","size","onClick"]),i.is_logged_in?(p(),B(fe,{key:1,round:"",icon:"logout",class:"q-ml-sm",size:e.$q.screen.gt.sm?"md":"sm",loading:i.is_logout_progress,onClick:m[1]||(m[1]=t=>r.on_logout_click())},{default:b(()=>[z(pe,null,{default:b(()=>[K(G(e.$t("label.logout")),1)]),_:1})]),_:1},8,["size","loading"])):W("",!0)]),_:1})]),_:1},8,["style"]),i.menus?.length?(p(),B(Ft,{key:0,modelValue:i.is_show_menu,"onUpdate:modelValue":m[2]||(m[2]=t=>i.is_show_menu=t),bordered:"",elevated:"",width:300,breakpoint:400,class:"background-drawer"},{default:b(()=>[z(Nt,{class:ve("fit q-pt-sm")},{default:b(()=>[z(jt,null,{default:b(()=>[(p(!0),ue(we,null,Pe(i.menus,(t,q)=>(p(),ue(we,{key:q},[t.separator?(p(),B(be,{key:0})):t.children?.length?(p(),B(Ne,{key:1,"default-opened":i.active_menu.parent&&t.id===i.active_menu.parent.id||i.active_menu.parent?.parent&&t.id===i.active_menu.parent.parent.id},{header:b(()=>[z(M,{avatar:"",style:{"min-width":"0px !important"}},{default:b(()=>[z(te,{name:t.icon},null,8,["name"])]),_:2},1024),z(M,null,{default:b(()=>[he("div",null,[K(G(t.title)+" ",1),t.badge===!0?(p(),B(me,{key:0,color:"orange",rounded:"",align:"top",transparent:""})):W("",!0)])]),_:2},1024)]),default:b(()=>[(p(!0),ue(we,null,Pe(t.children,x=>(p(),ue("div",{key:x.id,class:"drawer-child"},[x.separator?(p(),B(be,{key:0})):x.children?.length?(p(),B(Ne,{key:1,"default-opened":i.active_menu.parent&&x.id===i.active_menu.parent.id},{header:b(()=>[z(M,{avatar:"",style:{"min-width":"0px !important","padding-left":"24px"}},{default:b(()=>[z(te,{name:x.icon},null,8,["name"])]),_:2},1024),z(M,null,{default:b(()=>[he("div",null,[K(G(x.title)+" ",1),x.badge===!0?(p(),B(me,{key:0,color:"orange",rounded:"",align:"top",transparent:""})):W("",!0)])]),_:2},1024)]),default:b(()=>[(p(!0),ue(we,null,Pe(x.children,l=>(p(),ue("div",{key:l.id},[l.separator?(p(),B(be,{key:0})):ie((p(),B(ge,{key:1,clickable:"",onClick:v=>r.on_menu_click(l)},{default:b(()=>[z(M,{avatar:"",style:{"min-width":"0px !important","padding-left":"48px"}},{default:b(()=>[z(te,{name:l.icon},null,8,["name"])]),_:2},1024),z(M,{class:ve(i.active_menu.id===l.id?"text-weight-bold":"")},{default:b(()=>[he("div",null,[K(G(l.title)+" ",1),l.badge===!0?(p(),B(me,{key:0,color:"orange",rounded:"",align:"top",transparent:""})):W("",!0)])]),_:2},1032,["class"])]),_:2},1032,["onClick"])),[[Be]])]))),128))]),_:2},1032,["default-opened"])):ie((p(),B(ge,{key:2,clickable:"",onClick:l=>r.on_menu_click(x)},{default:b(()=>[z(M,{avatar:"",style:{"min-width":"0px !important","padding-left":"24px"}},{default:b(()=>[z(te,{name:x.icon},null,8,["name"])]),_:2},1024),z(M,{class:ve(i.active_menu.id===x.id?"text-weight-bold":"")},{default:b(()=>[he("div",null,[K(G(x.title)+" ",1),x.badge===!0?(p(),B(me,{key:0,color:"orange",rounded:"",align:"top",transparent:""})):W("",!0)])]),_:2},1032,["class"])]),_:2},1032,["onClick"])),[[Be]])]))),128))]),_:2},1032,["default-opened"])):ie((p(),B(ge,{key:2,clickable:"",onClick:x=>r.on_menu_click(t)},{default:b(()=>[z(M,{avatar:"",style:{margin:"0px !important",padding:"0px !important","min-width":"0px !important"}},{default:b(()=>[z(te,{name:t.icon},null,8,["name"])]),_:2},1024),z(M,{class:ve("q-pl-md"+(i.active_menu.id===t.id?" text-weight-bold":""))},{default:b(()=>[K(G(t.title),1)]),_:2},1032,["class"])]),_:2},1032,["onClick"])),[[Be]])],64))),128))]),_:1})]),_:1})]),_:1},8,["modelValue"])):W("",!0),z(Ht,null,{default:b(()=>[z($t,null,{default:b(()=>[i.active_menu?.title?(p(),B(ge,{key:0,class:"q-pa-sm q-pl-md q-pb-sm text-h5"},{default:b(()=>[i.active_menu.icon?(p(),B(M,{key:0,avatar:"",style:{"min-width":"0px !important"}},{default:b(()=>[z(te,{name:i.active_menu.icon},null,8,["name"])]),_:1})):W("",!0),z(M,null,{default:b(()=>[he("div",null,[K(G(i.active_menu.title)+" ",1),i.active_menu.badge===!0?(p(),B(me,{key:0,color:"orange",rounded:"",align:"top",transparent:""})):W("",!0)])]),_:1})]),_:1})):W("",!0),z(n),z(Ut,{position:"bottom-right","scroll-offset":120,offset:[12,12]},{default:b(()=>[z(fe,{round:"",glossy:"",size:"sm",icon:"keyboard_arrow_up",color:"primary"})]),_:1})]),_:1})]),_:1})]),_:1})}const ia=Ot(Yt,[["render",Kt]]);export{ia as default};
