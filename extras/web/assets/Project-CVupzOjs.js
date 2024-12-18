const __vite__mapDeps=(i,m=__vite__mapDeps,d=(m.f||(m.f=["assets/Module-NHHILIfX.js","assets/QTooltip-DTsmNrTK.js","assets/index-3Ds9K3sY.js","assets/index-DH4Oj3ZA.css","assets/QImg-CPhRzbPa.js","assets/QSelect-ybIcc9Im.js","assets/format-BYB7LI8Y.js","assets/QForm-DrU6uDMp.js","assets/ClosePopup-Cf-fI5V5.js"])))=>i.map(i=>d[i]);
import{k as le,t as te,ad as ne,x as ie,c as s,r as b,w as oe,ae as I,af as se,ag as ue,h as P,ah as W,g as re,a5 as M,_ as de,ai as ce,Y as j,Z as L,aa as D,aj as d,f as r,a4 as v,a1 as N,a8 as me,a7 as h,F as U,ab as ge,ak as ve,a2 as be,a3 as f,a6 as Y,E as z,al as K,am as fe,an as ye}from"./index-3Ds9K3sY.js";import{Q as he}from"./QSelect-ybIcc9Im.js";import{Q as _e}from"./QTooltip-DTsmNrTK.js";import{Q as pe}from"./QForm-DrU6uDMp.js";import{c as xe}from"./format-BYB7LI8Y.js";import{Q as Se}from"./QImg-CPhRzbPa.js";function q(e,n){return[!0,!1].includes(e)?e:n}const ke=le({name:"QPagination",props:{...te,modelValue:{type:Number,required:!0},min:{type:[Number,String],default:1},max:{type:[Number,String],required:!0},maxPages:{type:[Number,String],default:0,validator:e=>(typeof e=="string"?parseInt(e,10):e)>=0},inputStyle:[Array,String,Object],inputClass:[Array,String,Object],size:String,disable:Boolean,input:Boolean,iconPrev:String,iconNext:String,iconFirst:String,iconLast:String,toFn:Function,boundaryLinks:{type:Boolean,default:null},boundaryNumbers:{type:Boolean,default:null},directionLinks:{type:Boolean,default:null},ellipses:{type:Boolean,default:null},ripple:{type:[Boolean,Object],default:null},round:Boolean,rounded:Boolean,flat:Boolean,outline:Boolean,unelevated:Boolean,push:Boolean,glossy:Boolean,color:{type:String,default:"primary"},textColor:String,activeDesign:{type:String,default:"",values:e=>e===""||ne.includes(e)},activeColor:String,activeTextColor:String,gutter:String,padding:{type:String,default:"3px 2px"}},emits:["update:modelValue"],setup(e,{emit:n}){const{proxy:m}=re(),{$q:a}=m,_=ie(e,a),i=s(()=>parseInt(e.min,10)),u=s(()=>parseInt(e.max,10)),o=s(()=>parseInt(e.maxPages,10)),w=s(()=>p.value+" / "+u.value),E=s(()=>q(e.boundaryLinks,e.input)),x=s(()=>q(e.boundaryNumbers,!e.input)),R=s(()=>q(e.directionLinks,e.input)),F=s(()=>q(e.ellipses,!e.input)),C=b(null),p=s({get:()=>e.modelValue,set:l=>{if(l=parseInt(l,10),e.disable||isNaN(l))return;const t=xe(l,i.value,u.value);e.modelValue!==t&&n("update:modelValue",t)}});oe(()=>`${i.value}|${u.value}`,()=>{p.value=e.modelValue});const X=s(()=>"q-pagination row no-wrap items-center"+(e.disable===!0?" disabled":"")),Q=s(()=>e.gutter in I?`${I[e.gutter]}px`:e.gutter||null),Z=s(()=>Q.value!==null?`--q-pagination-gutter-parent:-${Q.value};--q-pagination-gutter-child:${Q.value}`:null),B=s(()=>{const l=[e.iconFirst||a.iconSet.pagination.first,e.iconPrev||a.iconSet.pagination.prev,e.iconNext||a.iconSet.pagination.next,e.iconLast||a.iconSet.pagination.last];return a.lang.rtl===!0?l.reverse():l}),G=s(()=>({"aria-disabled":e.disable===!0?"true":"false",role:"navigation"})),O=s(()=>se(e,"flat")),H=s(()=>({[O.value]:!0,round:e.round,rounded:e.rounded,padding:e.padding,color:e.color,textColor:e.textColor,size:e.size,ripple:e.ripple!==null?e.ripple:!0})),J=s(()=>{const l={[O.value]:!1};return e.activeDesign!==""&&(l[e.activeDesign]=!0),l}),$=s(()=>({...J.value,color:e.activeColor||e.color,textColor:e.activeTextColor||e.textColor})),S=s(()=>{let l=Math.max(o.value,1+(F.value?2:0)+(x.value?2:0));const t={pgFrom:i.value,pgTo:u.value,ellipsesStart:!1,ellipsesEnd:!1,boundaryStart:!1,boundaryEnd:!1,marginalStyle:{minWidth:`${Math.max(2,String(u.value).length)}em`}};return o.value&&l<u.value-i.value+1&&(l=1+Math.floor(l/2)*2,t.pgFrom=Math.max(i.value,Math.min(u.value-l+1,e.modelValue-Math.floor(l/2))),t.pgTo=Math.min(u.value,t.pgFrom+l-1),x.value&&(t.boundaryStart=!0,t.pgFrom++),F.value&&t.pgFrom>i.value+(x.value?1:0)&&(t.ellipsesStart=!0,t.pgFrom++),x.value&&(t.boundaryEnd=!0,t.pgTo--),F.value&&t.pgTo<u.value-(x.value?1:0)&&(t.ellipsesEnd=!0,t.pgTo--)),t});function T(l){p.value=l}function ee(l){p.value=p.value+l}const ae=s(()=>{function l(){p.value=C.value,C.value=null}return{"onUpdate:modelValue":t=>{C.value=t},onKeyup:t=>{ue(t,13)===!0&&l()},onBlur:l}});function g(l,t,k){const y={"aria-label":t,"aria-current":"false",...H.value,...l};return k===!0&&Object.assign(y,{"aria-current":"true",...$.value}),t!==void 0&&(e.toFn!==void 0?y.to=e.toFn(t):y.onClick=()=>{T(t)}),P(M,y)}return Object.assign(m,{set:T,setByOffset:ee}),()=>{const l=[],t=[];let k;if(E.value===!0&&(l.push(g({key:"bls",disable:e.disable||e.modelValue<=i.value,icon:B.value[0]},i.value)),t.unshift(g({key:"ble",disable:e.disable||e.modelValue>=u.value,icon:B.value[3]},u.value))),R.value===!0&&(l.push(g({key:"bdp",disable:e.disable||e.modelValue<=i.value,icon:B.value[1]},e.modelValue-1)),t.unshift(g({key:"bdn",disable:e.disable||e.modelValue>=u.value,icon:B.value[2]},e.modelValue+1))),e.input!==!0){k=[];const{pgFrom:y,pgTo:A,marginalStyle:V}=S.value;if(S.value.boundaryStart===!0){const c=i.value===e.modelValue;l.push(g({key:"bns",style:V,disable:e.disable,label:i.value},i.value,c))}if(S.value.boundaryEnd===!0){const c=u.value===e.modelValue;t.unshift(g({key:"bne",style:V,disable:e.disable,label:u.value},u.value,c))}S.value.ellipsesStart===!0&&l.push(g({key:"bes",style:V,disable:e.disable,label:"…",ripple:!1},y-1)),S.value.ellipsesEnd===!0&&t.unshift(g({key:"bee",style:V,disable:e.disable,label:"…",ripple:!1},A+1));for(let c=y;c<=A;c++)k.push(g({key:`bpg${c}`,style:V,disable:e.disable,label:c},c,c===e.modelValue))}return P("div",{class:X.value,...G.value},[P("div",{class:"q-pagination__content row no-wrap items-center",style:Z.value},[...l,e.input===!0?P(W,{class:"inline",style:{width:`${w.value.length/1.5}em`},type:"number",dense:!0,value:C.value,disable:e.disable,dark:_.value,borderless:!0,inputClass:e.inputClass,inputStyle:e.inputStyle,placeholder:w.value,min:i.value,max:u.value,...ae.value}):P("div",{class:"q-pagination__middle row justify-center"},k),...t])])}}}),Ve="/ui/assets/noimage-DnPM5X0E.png",Pe={components:{Module:ce(()=>ye(()=>import("./Module-NHHILIfX.js"),__vite__mapDeps([0,1,2,3,4,5,6,7,8])))},setup(){return{util:j,api:L,initialized:b(!1),page:b({value:1,size:18,min:1,max:3}),order:b("name"),search:b(null),projects:b([]),loading:b(!1),sorts:b([{label:"Name (Asc)",value:"name"},{label:"Name (Desc)",value:"-name"},{label:"Date (Asc)",value:"createdOn"},{label:"Date (Desc)",value:"-createdOn"}]),dialog:b({module:{show:!1,parameters:null}})}},created(){this.get_projects()},methods:{get_projects(){let e=this;e.loading=!0,L.call({path:"/projects",method:"post",params:{index:e.page.value,size:e.page.size,order:e.order,search:e.search},onFinish(){e.loading=!1,e.initialized=!0},onSuccess(n){let m=1,a=1,_=[];j.isObject(n)&&(j.isNumber(n.index)&&(m=n.index-1,m<1&&(m=1)),a=m+2,_=j.isArray(n.data)?n.data:[],_.length<e.page.size&&(a=e.page.value));let i=e.page;i.min=m,i.max=a,e.projects=_}})},on_update_page(e){let n=this;n.page.value=e,n.get_projects()},on_sort_update(e){let n=this;n.order=e,n.get_projects()},on_dialog_module(e){let n=this;n.dialog.module={show:!0,parameters:{project:e}}}}},we={class:"row q-pa-sm justify-end"},Ce={key:0},Be={class:"flex flex-center"},je={class:"login-form text-center"},De={class:"text-h6"},Ne={key:1,class:"row q-pa-sm"},qe={class:"row no-wrap items-center"},Fe={class:"col text-caption q-pt-md"},Qe={class:"col-auto text-grey text-caption q-pt-md row no-wrap items-center"},ze={class:"text-subtitle1"},Me={class:"text-caption text-grey ellipsis-text lines4"};function Ee(e,n,m,a,_,i){const u=be("Module");return f(),D(U,null,[d("div",null,[d("div",we,[r(he,{modelValue:a.order,"onUpdate:modelValue":[n[0]||(n[0]=o=>a.order=o),i.on_sort_update],label:e.$t("label.sort"),options:a.sorts,disable:a.loading,"option-value":"value","option-label":"label",borderless:"",dense:"","emit-value":"","map-options":"",class:"q-mr-sm"},null,8,["modelValue","label","options","disable","onUpdate:modelValue"]),r(pe,{onSubmit:i.get_projects,class:"q-mr-sm"},{default:v(()=>[r(W,{modelValue:a.search,"onUpdate:modelValue":n[1]||(n[1]=o=>a.search=o),label:e.$t("label.search"),disable:a.loading,outlined:"",dense:"",clearable:""},{append:v(()=>[r(M,{round:"",dense:"",glossy:"",icon:"search",onClick:i.get_projects},{default:v(()=>[r(_e,null,{default:v(()=>[Y(h(e.$t("label.search")),1)]),_:1})]),_:1},8,["onClick"])]),_:1},8,["modelValue","label","disable"])]),_:1},8,["onSubmit"]),a.projects.length===a.page.size||a.page.value>1?(f(),N(ke,{key:0,modelValue:a.page.value,"onUpdate:modelValue":[n[2]||(n[2]=o=>a.page.value=o),i.on_update_page],"icon-prev":"arrow_back_ios","icon-next":"arrow_forward_ios","direction-links":"",min:a.page.min,max:a.page.max,disable:a.loading},null,8,["modelValue","min","max","disable","onUpdate:modelValue"])):me("",!0)]),a.initialized&&!a.projects?.length?(f(),D("div",Ce,[d("div",Be,[d("div",je,[r(z,{name:"mood_bad",color:"deep-orange-11",size:"200px"}),d("div",De,h(e.$t("label.unavailable_content")),1)])])])):(f(),D("div",Ne,[(f(!0),D(U,null,ge(a.projects,(o,w)=>(f(),N(fe,{key:w,class:"col-lg-3 col-md-4 col-sm-6 col-xs-12 q-pa-xs q-mb-md"},{default:v(()=>[o.icon&&o.icon.startsWith("icon:")?(f(),N(z,{key:0,name:o.icon.substring(5),size:"200px",style:{width:"100%"}},null,8,["name"])):(f(),N(Se,{key:1,src:a.api.multimedia(o.icon),height:"200px",fit:"fill"},{error:v(()=>n[4]||(n[4]=[d("img",{src:Ve,style:{width:"100%",height:"200px","object-fit":"contain"},alt:""},null,-1)])),_:2},1032,["src"])),r(K,null,{default:v(()=>[r(M,{fab:"",glossy:"",color:"teal",icon:"hub",class:"absolute",style:{top:"0",right:"12px",transform:"translateY(-50%)"},onClick:E=>i.on_dialog_module(o)},null,8,["onClick"]),d("div",qe,[d("div",Fe,[r(z,{name:"radio_button_checked",color:o.isActive==="Y"?"positive":"negative"},null,8,["color"]),Y(" "+h(o.isActive==="Y"?e.$t("label.active"):e.$t("label.inactive")),1)]),d("div",Qe,h(e.$t("label.module"))+" ("+h(o.modules)+") ",1)])]),_:2},1024),r(K,{class:"q-pt-none"},{default:v(()=>[d("div",ze,h(o.name),1),d("div",Me,h(o.description),1)]),_:2},1024)]),_:2},1024))),128))]))]),r(ve,{modelValue:a.dialog.module.show,"onUpdate:modelValue":n[3]||(n[3]=o=>a.dialog.module.show=o),"transition-show":"slide-down","transition-hide":"none","full-width":"","full-height":""},{default:v(()=>[r(u,{parameters:a.dialog.module.parameters},null,8,["parameters"])]),_:1},8,["modelValue"])],64)}const Oe=de(Pe,[["render",Ee]]),Ke=Object.freeze(Object.defineProperty({__proto__:null,default:Oe},Symbol.toStringTag,{value:"Module"}));export{Ke as P,ke as Q,Ve as _};