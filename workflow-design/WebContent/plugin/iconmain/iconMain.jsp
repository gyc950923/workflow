<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	request.setAttribute("rootpath", request.getContextPath());
	String basePath = request.getServerName() + ":"
			+ request.getServerPort();
	request.setAttribute("ipport", basePath);
%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>鼎信诺开发平台</title>
<link rel="stylesheet"
	href="${rootpath}/plugin/Font-Awesome-3.2.1/css/font-awesome.min.css" />
<!--<meta name="viewport" content="width=device-width; initial-scale=1; maximum-scale=1">-->
<!--[if lt IE 9]>
  <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
  <![endif]-->
<!-- CSS
 ================================================== -->
<LINK rel="stylesheet" href="${rootpath}/plugin/iconmain/site.css">
<LINK rel="stylesheet" href="${rootpath}/plugin/iconmain/prettify.css">
		<!--[if IE 7]>
  <link rel="stylesheet" href="assets/css/font-awesome-ie7.min.css">
  <![endif]-->
		<!-- Le fav and touch icons -->

<script type="text/javascript" src="${rootpath}/plugin/jquery/jquery-1.7.2.min.js"></script>
</head>

<body>
<DIV class="container">
<SECTION>
<DIV class="row">
<DIV class="span4">
<UL class="unstyled">
  <LI><I class="icon-"></I> icon-glass <SPAN class="muted">(&amp;#xf000;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-music <SPAN class="muted">(&amp;#xf001;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-search <SPAN class="muted">(&amp;#xf002;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-envelope <SPAN class="muted">(&amp;#xf003;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-heart <SPAN class="muted">(&amp;#xf004;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-star <SPAN class="muted">(&amp;#xf005;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-star-empty <SPAN class="muted">(&amp;#xf006;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-user <SPAN class="muted">(&amp;#xf007;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-film <SPAN class="muted">(&amp;#xf008;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-th-large <SPAN class="muted">(&amp;#xf009;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-th <SPAN class="muted">(&amp;#xf00a;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-th-list <SPAN class="muted">(&amp;#xf00b;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-ok <SPAN class="muted">(&amp;#xf00c;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-remove <SPAN class="muted">(&amp;#xf00d;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-zoom-in <SPAN class="muted">(&amp;#xf00e;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-zoom-out <SPAN class="muted">(&amp;#xf010;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-off <SPAN class="muted">(&amp;#xf011;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-signal <SPAN class="muted">(&amp;#xf012;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-cog <SPAN class="muted">(&amp;#xf013;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-trash <SPAN class="muted">(&amp;#xf014;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-home <SPAN class="muted">(&amp;#xf015;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-file <SPAN class="muted">(&amp;#xf016;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-time <SPAN class="muted">(&amp;#xf017;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-road <SPAN class="muted">(&amp;#xf018;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-download-alt <SPAN class="muted">(&amp;#xf019;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-download <SPAN class="muted">(&amp;#xf01a;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-upload <SPAN class="muted">(&amp;#xf01b;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-inbox <SPAN class="muted">(&amp;#xf01c;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-play-circle <SPAN class="muted">(&amp;#xf01d;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-repeat <SPAN class="muted">(&amp;#xf01e;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-refresh <SPAN class="muted">(&amp;#xf021;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-list-alt <SPAN class="muted">(&amp;#xf022;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-lock <SPAN class="muted">(&amp;#xf023;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-flag <SPAN class="muted">(&amp;#xf024;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-headphones <SPAN class="muted">(&amp;#xf025;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-volume-off <SPAN class="muted">(&amp;#xf026;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-volume-down <SPAN class="muted">(&amp;#xf027;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-volume-up <SPAN class="muted">(&amp;#xf028;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-qrcode <SPAN class="muted">(&amp;#xf029;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-barcode <SPAN class="muted">(&amp;#xf02a;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-tag <SPAN class="muted">(&amp;#xf02b;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-tags <SPAN class="muted">(&amp;#xf02c;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-book <SPAN class="muted">(&amp;#xf02d;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-bookmark <SPAN class="muted">(&amp;#xf02e;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-print <SPAN class="muted">(&amp;#xf02f;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-camera <SPAN class="muted">(&amp;#xf030;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-font <SPAN class="muted">(&amp;#xf031;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-bold <SPAN class="muted">(&amp;#xf032;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-italic <SPAN class="muted">(&amp;#xf033;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-text-height <SPAN class="muted">(&amp;#xf034;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-text-width <SPAN class="muted">(&amp;#xf035;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-align-left <SPAN class="muted">(&amp;#xf036;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-align-center <SPAN class="muted">(&amp;#xf037;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-align-right <SPAN class="muted">(&amp;#xf038;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-align-justify <SPAN class="muted">(&amp;#xf039;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-list <SPAN class="muted">(&amp;#xf03a;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-indent-left <SPAN class="muted">(&amp;#xf03b;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-indent-right <SPAN class="muted">(&amp;#xf03c;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-facetime-video <SPAN
							class="muted">(&amp;#xf03d;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-picture <SPAN class="muted">(&amp;#xf03e;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-pencil <SPAN class="muted">(&amp;#xf040;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-map-marker <SPAN class="muted">(&amp;#xf041;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-adjust <SPAN class="muted">(&amp;#xf042;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-tint <SPAN class="muted">(&amp;#xf043;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-edit <SPAN class="muted">(&amp;#xf044;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-share <SPAN class="muted">(&amp;#xf045;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-check <SPAN class="muted">(&amp;#xf046;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-move <SPAN class="muted">(&amp;#xf047;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-step-backward <SPAN class="muted">(&amp;#xf048;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-fast-backward <SPAN class="muted">(&amp;#xf049;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-backward <SPAN class="muted">(&amp;#xf04a;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-play <SPAN class="muted">(&amp;#xf04b;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-pause <SPAN class="muted">(&amp;#xf04c;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-stop <SPAN class="muted">(&amp;#xf04d;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-forward <SPAN class="muted">(&amp;#xf04e;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-fast-forward <SPAN class="muted">(&amp;#xf050;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-step-forward <SPAN class="muted">(&amp;#xf051;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-eject <SPAN class="muted">(&amp;#xf052;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-chevron-left <SPAN class="muted">(&amp;#xf053;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-chevron-right <SPAN class="muted">(&amp;#xf054;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-plus-sign <SPAN class="muted">(&amp;#xf055;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-minus-sign <SPAN class="muted">(&amp;#xf056;)</SPAN></LI>
					</UL>
				</DIV>
<DIV class="span4">
<UL class="unstyled">
  <LI><I class="icon-"></I> icon-remove-sign <SPAN class="muted">(&amp;#xf057;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-ok-sign <SPAN class="muted">(&amp;#xf058;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-question-sign <SPAN class="muted">(&amp;#xf059;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-info-sign <SPAN class="muted">(&amp;#xf05a;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-screenshot <SPAN class="muted">(&amp;#xf05b;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-remove-circle <SPAN class="muted">(&amp;#xf05c;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-ok-circle <SPAN class="muted">(&amp;#xf05d;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-ban-circle <SPAN class="muted">(&amp;#xf05e;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-arrow-left <SPAN class="muted">(&amp;#xf060;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-arrow-right <SPAN class="muted">(&amp;#xf061;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-arrow-up <SPAN class="muted">(&amp;#xf062;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-arrow-down <SPAN class="muted">(&amp;#xf063;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-share-alt <SPAN class="muted">(&amp;#xf064;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-resize-full <SPAN class="muted">(&amp;#xf065;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-resize-small <SPAN class="muted">(&amp;#xf066;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-plus <SPAN class="muted">(&amp;#xf067;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-minus <SPAN class="muted">(&amp;#xf068;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-asterisk <SPAN class="muted">(&amp;#xf069;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-exclamation-sign <SPAN
							class="muted">(&amp;#xf06a;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-gift <SPAN class="muted">(&amp;#xf06b;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-leaf <SPAN class="muted">(&amp;#xf06c;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-fire <SPAN class="muted">(&amp;#xf06d;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-eye-open <SPAN class="muted">(&amp;#xf06e;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-eye-close <SPAN class="muted">(&amp;#xf070;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-warning-sign <SPAN class="muted">(&amp;#xf071;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-plane <SPAN class="muted">(&amp;#xf072;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-calendar <SPAN class="muted">(&amp;#xf073;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-random <SPAN class="muted">(&amp;#xf074;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-comment <SPAN class="muted">(&amp;#xf075;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-magnet <SPAN class="muted">(&amp;#xf076;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-chevron-up <SPAN class="muted">(&amp;#xf077;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-chevron-down <SPAN class="muted">(&amp;#xf078;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-retweet <SPAN class="muted">(&amp;#xf079;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-shopping-cart <SPAN class="muted">(&amp;#xf07a;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-folder-close <SPAN class="muted">(&amp;#xf07b;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-folder-open <SPAN class="muted">(&amp;#xf07c;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-resize-vertical <SPAN
							class="muted">(&amp;#xf07d;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-resize-horizontal <SPAN
							class="muted">(&amp;#xf07e;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-bar-chart <SPAN class="muted">(&amp;#xf080;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-twitter-sign <SPAN class="muted">(&amp;#xf081;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-facebook-sign <SPAN class="muted">(&amp;#xf082;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-camera-retro <SPAN class="muted">(&amp;#xf083;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-key <SPAN class="muted">(&amp;#xf084;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-cogs <SPAN class="muted">(&amp;#xf085;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-comments <SPAN class="muted">(&amp;#xf086;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-thumbs-up <SPAN class="muted">(&amp;#xf087;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-thumbs-down <SPAN class="muted">(&amp;#xf088;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-star-half <SPAN class="muted">(&amp;#xf089;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-heart-empty <SPAN class="muted">(&amp;#xf08a;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-signout <SPAN class="muted">(&amp;#xf08b;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-linkedin-sign <SPAN class="muted">(&amp;#xf08c;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-pushpin <SPAN class="muted">(&amp;#xf08d;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-external-link <SPAN class="muted">(&amp;#xf08e;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-signin <SPAN class="muted">(&amp;#xf090;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-trophy <SPAN class="muted">(&amp;#xf091;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-github-sign <SPAN class="muted">(&amp;#xf092;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-upload-alt <SPAN class="muted">(&amp;#xf093;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-lemon <SPAN class="muted">(&amp;#xf094;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-phone <SPAN class="muted">(&amp;#xf095;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-check-empty <SPAN class="muted">(&amp;#xf096;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-bookmark-empty <SPAN
							class="muted">(&amp;#xf097;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-phone-sign <SPAN class="muted">(&amp;#xf098;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-twitter <SPAN class="muted">(&amp;#xf099;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-facebook <SPAN class="muted">(&amp;#xf09a;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-github <SPAN class="muted">(&amp;#xf09b;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-unlock <SPAN class="muted">(&amp;#xf09c;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-credit-card <SPAN class="muted">(&amp;#xf09d;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-rss <SPAN class="muted">(&amp;#xf09e;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-hdd <SPAN class="muted">(&amp;#xf0a0;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-bullhorn <SPAN class="muted">(&amp;#xf0a1;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-bell <SPAN class="muted">(&amp;#xf0a2;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-certificate <SPAN class="muted">(&amp;#xf0a3;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-hand-right <SPAN class="muted">(&amp;#xf0a4;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-hand-left <SPAN class="muted">(&amp;#xf0a5;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-hand-up <SPAN class="muted">(&amp;#xf0a6;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-hand-down <SPAN class="muted">(&amp;#xf0a7;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-circle-arrow-left <SPAN
							class="muted">(&amp;#xf0a8;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-circle-arrow-right <SPAN
							class="muted">(&amp;#xf0a9;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-circle-arrow-up <SPAN
							class="muted">(&amp;#xf0aa;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-circle-arrow-down <SPAN
							class="muted">(&amp;#xf0ab;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-globe <SPAN class="muted">(&amp;#xf0ac;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-wrench <SPAN class="muted">(&amp;#xf0ad;)</SPAN></LI>
					</UL>
				</DIV>
<DIV class="span4">
<UL class="unstyled">
  <LI><I class="icon-"></I> icon-tasks <SPAN class="muted">(&amp;#xf0ae;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-filter <SPAN class="muted">(&amp;#xf0b0;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-briefcase <SPAN class="muted">(&amp;#xf0b1;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-fullscreen <SPAN class="muted">(&amp;#xf0b2;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-group <SPAN class="muted">(&amp;#xf0c0;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-link <SPAN class="muted">(&amp;#xf0c1;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-cloud <SPAN class="muted">(&amp;#xf0c2;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-beaker <SPAN class="muted">(&amp;#xf0c3;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-cut <SPAN class="muted">(&amp;#xf0c4;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-copy <SPAN class="muted">(&amp;#xf0c5;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-paper-clip <SPAN class="muted">(&amp;#xf0c6;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-save <SPAN class="muted">(&amp;#xf0c7;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-sign-blank <SPAN class="muted">(&amp;#xf0c8;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-reorder <SPAN class="muted">(&amp;#xf0c9;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-list-ul <SPAN class="muted">(&amp;#xf0ca;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-list-ol <SPAN class="muted">(&amp;#xf0cb;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-strikethrough <SPAN class="muted">(&amp;#xf0cc;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-underline <SPAN class="muted">(&amp;#xf0cd;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-table <SPAN class="muted">(&amp;#xf0ce;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-magic <SPAN class="muted">(&amp;#xf0d0;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-truck <SPAN class="muted">(&amp;#xf0d1;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-pinterest <SPAN class="muted">(&amp;#xf0d2;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-pinterest-sign <SPAN
							class="muted">(&amp;#xf0d3;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-google-plus-sign <SPAN
							class="muted">(&amp;#xf0d4;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-google-plus <SPAN class="muted">(&amp;#xf0d5;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-money <SPAN class="muted">(&amp;#xf0d6;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-caret-down <SPAN class="muted">(&amp;#xf0d7;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-caret-up <SPAN class="muted">(&amp;#xf0d8;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-caret-left <SPAN class="muted">(&amp;#xf0d9;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-caret-right <SPAN class="muted">(&amp;#xf0da;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-columns <SPAN class="muted">(&amp;#xf0db;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-sort <SPAN class="muted">(&amp;#xf0dc;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-sort-down <SPAN class="muted">(&amp;#xf0dd;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-sort-up <SPAN class="muted">(&amp;#xf0de;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-envelope-alt <SPAN class="muted">(&amp;#xf0e0;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-linkedin <SPAN class="muted">(&amp;#xf0e1;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-undo <SPAN class="muted">(&amp;#xf0e2;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-legal <SPAN class="muted">(&amp;#xf0e3;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-dashboard <SPAN class="muted">(&amp;#xf0e4;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-comment-alt <SPAN class="muted">(&amp;#xf0e5;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-comments-alt <SPAN class="muted">(&amp;#xf0e6;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-bolt <SPAN class="muted">(&amp;#xf0e7;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-sitemap <SPAN class="muted">(&amp;#xf0e8;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-umbrella <SPAN class="muted">(&amp;#xf0e9;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-paste <SPAN class="muted">(&amp;#xf0ea;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-lightbulb <SPAN class="muted">(&amp;#xf0eb;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-exchange <SPAN class="muted">(&amp;#xf0ec;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-cloud-download <SPAN
							class="muted">(&amp;#xf0ed;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-cloud-upload <SPAN class="muted">(&amp;#xf0ee;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-user-md <SPAN class="muted">(&amp;#xf0f0;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-stethoscope <SPAN class="muted">(&amp;#xf0f1;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-suitcase <SPAN class="muted">(&amp;#xf0f2;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-bell-alt <SPAN class="muted">(&amp;#xf0f3;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-coffee <SPAN class="muted">(&amp;#xf0f4;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-food <SPAN class="muted">(&amp;#xf0f5;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-file-alt <SPAN class="muted">(&amp;#xf0f6;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-building <SPAN class="muted">(&amp;#xf0f7;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-hospital <SPAN class="muted">(&amp;#xf0f8;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-ambulance <SPAN class="muted">(&amp;#xf0f9;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-medkit <SPAN class="muted">(&amp;#xf0fa;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-fighter-jet <SPAN class="muted">(&amp;#xf0fb;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-beer <SPAN class="muted">(&amp;#xf0fc;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-h-sign <SPAN class="muted">(&amp;#xf0fd;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-plus-sign-alt <SPAN class="muted">(&amp;#xf0fe;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-double-angle-left <SPAN
							class="muted">(&amp;#xf100;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-double-angle-right <SPAN
							class="muted">(&amp;#xf101;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-double-angle-up <SPAN
							class="muted">(&amp;#xf102;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-double-angle-down <SPAN
							class="muted">(&amp;#xf103;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-angle-left <SPAN class="muted">(&amp;#xf104;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-angle-right <SPAN class="muted">(&amp;#xf105;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-angle-up <SPAN class="muted">(&amp;#xf106;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-angle-down <SPAN class="muted">(&amp;#xf107;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-desktop <SPAN class="muted">(&amp;#xf108;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-laptop <SPAN class="muted">(&amp;#xf109;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-tablet <SPAN class="muted">(&amp;#xf10a;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-mobile-phone <SPAN class="muted">(&amp;#xf10b;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-circle-blank <SPAN class="muted">(&amp;#xf10c;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-quote-left <SPAN class="muted">(&amp;#xf10d;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-quote-right <SPAN class="muted">(&amp;#xf10e;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-spinner <SPAN class="muted">(&amp;#xf110;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-circle <SPAN class="muted">(&amp;#xf111;)</SPAN></LI>
  <LI><I class="icon-"></I> icon-reply <SPAN class="muted">(&amp;#xf112;)</SPAN></LI>
					</UL>
				</DIV>
			</DIV>
		</SECTION>
	</DIV>

<SCRIPT src="${rootpath}/plugin/iconmain/bootstrap.min.js"></SCRIPT>

</body>
</html>