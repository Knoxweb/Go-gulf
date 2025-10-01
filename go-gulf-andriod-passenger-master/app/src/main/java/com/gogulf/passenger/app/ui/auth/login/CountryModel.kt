package com.gogulf.passenger.app.ui.auth.login

import android.content.Context
import android.telephony.TelephonyManager
import com.gogulf.passenger.app.App
import com.gogulf.passenger.app.R

data class CountryModel(
    val id: String,
    val name: String,
    val flag: String,
    val code: String,
    val dialCode: String,
    val pattern: String,
    val limit: Int,
    val drawable: Int
)


val countryList = listOf(
    CountryModel(
        "0001",
        "Afghanistan",
        "🇦🇫",
        "AF",
        "+93",
        "### ### ###",
        17,
        R.drawable.flag_afghanistan__af_
    ), CountryModel(
        "0003", "Albania", "🇦🇱", "AL", "+355", "## ### ####", 17, R.drawable.flag_albania__al_
    ), CountryModel(
        "0004", "Algeria", "🇩🇿", "DZ", "+213", "### ## ## ##", 17, R.drawable.flag_algeria__dz_
    ), CountryModel(
        "0005",
        "American Samoa",
        "🇦🇸",
        "AS",
        "+1684",
        "### ####",
        17,
        R.drawable.flag_american_samoa__as_
    ), CountryModel(
        "0006", "Andorra", "🇦🇩", "AD", "+376", "## ## ##", 17, R.drawable.flag_andorra__ad_
    ), CountryModel(
        "0007", "Angola", "🇦🇴", "AO", "+244", "### ### ###", 17, R.drawable.flag_angola__ao_
    ), CountryModel(
        "0008", "Anguilla", "🇦🇮", "AI", "+1264", "### ####", 17, R.drawable.flag_anguilla__ai_
    ), CountryModel(
        "0010",
        "Antigua & Barbuda",
        "🇦🇬",
        "AG",
        "+1268",
        "### ####",
        17,
        R.drawable.flag_antigua_and_barbuda__ag_
    ), CountryModel(
        "0011", "Argentina", "🇦🇷", "AR", "+54", "#", 17, R.drawable.flag_argentina__ar_
    ), CountryModel(
        "0012", "Armenia", "🇦🇲", "AM", "+374", "## ### ###", 17, R.drawable.flag_armenia__am_
    ), CountryModel(
        "0013", "Aruba", "🇦🇼", "AW", "+297", "### ####", 17, R.drawable.flag_aruba__aw_
    ), CountryModel(
        "0014", "Australia", "🇦🇺", "AU", "+61", "# #### ####", 17, R.drawable.flag_australia__au_
    ), CountryModel(
        "0015", "Austria", "🇦🇹", "AT", "+43", "### ######", 17, R.drawable.flag_austria__at_
    ), CountryModel(
        "0016", "Azerbaijan", "🇦🇿", "AZ", "+994", "## ### ####", 17, R.drawable.flag_azerbaijan__az_
    ), CountryModel(
        "0017", "Bahamas", "🇧🇸", "BS", "+1242", "### ####", 17, R.drawable.flag_bahamas__bs_
    ), CountryModel(
        "0018", "Bahrain", "🇧🇭", "BH", "+973", "#### ####", 17, R.drawable.flag_bahrain__bh_
    ), CountryModel(
        "0019", "Bangladesh", "🇧🇩", "BD", "+880", "## ### ###", 17, R.drawable.flag_bangladesh__bd_
    ), CountryModel(
        "0020", "Barbados", "🇧🇧", "BB", "+1246", "### ####", 17, R.drawable.flag_barbados__bb_
    ), CountryModel(
        "0021", "Belarus", "🇧🇾", "BY", "+375", "## ### ####", 17, R.drawable.flag_belarus__by_
    ), CountryModel(
        "0022", "Belgium", "🇧🇪", "BE", "+32", "### ## ## ##", 17, R.drawable.flag_belgium__be_
    ), CountryModel(
        "0023", "Belize", "🇧🇿", "BZ", "+501", "#", 17, R.drawable.flag_belize__bz_
    ), CountryModel(
        "0024", "Benin", "🇧🇯", "BJ", "+229", "## ### ###", 17, R.drawable.flag_benin__bj_
    ), CountryModel(
        "0025", "Bermuda", "🇧🇲", "BM", "+1441", "### ####", 17, R.drawable.flag_bermuda__bm_
    ), CountryModel(
        "0026", "Bhutan", "🇧🇹", "BT", "+975", "## ### ###", 17, R.drawable.flag_bhutan__bt_
    ), CountryModel(
        "0027", "Bolivia", "🇧🇴", "BO", "+591", "# ### ####", 17, R.drawable.flag_bolivia__bo_
    ), CountryModel(
        "0028",
        "Bosnia & Herzegovina",
        "🇧🇦",
        "BA",
        "+387",
        "## ### ###",
        17,
        R.drawable.flag_bosnia_and_herzegovina__ba_
    ), CountryModel(
        "0029", "Botswana", "🇧🇼", "BW", "+267", "## ### ###", 17, R.drawable.flag_botswana__bw_
    ), CountryModel(
        "0031", "Brazil", "🇧🇷", "BR", "+55", "## ##### ####", 17, R.drawable.flag_brazil__br_
    ), CountryModel(
        "0032",
        "British Virgin Islands",
        "🇻🇬",
        "IO",
        "+1284",
        "### ####",
        17,
        R.drawable.flag_virgin_islands__british___vg_
    ), CountryModel(
        "0033",
        "Brunei Darussalam",
        "🇧🇳",
        "BN",
        "+673",
        "### ####",
        17,
        R.drawable.flag_brunei_darussalam__bn_
    ), CountryModel(
        "0034", "Bulgaria", "🇧🇬", "BG", "+359", "#", 17, R.drawable.flag_bulgaria__bg_
    ), CountryModel(
        "0035",
        "Burkina Faso",
        "🇧🇫",
        "BF",
        "+226",
        "## ## ## ##",
        17,
        R.drawable.flag_burkina_faso__bf_
    ), CountryModel(
        "0036", "Burundi", "🇧🇮", "BI", "+257", "## ## ####", 17, R.drawable.flag_burundi__bi_
    ), CountryModel(
        "0037", "Cambodia", "🇰🇭", "KH", "+855", "## ### ###", 17, R.drawable.flag_cambodia__kh_
    ), CountryModel(
        "0038", "Cameroon", "🇨🇲", "CM", "+237", "#### ####", 17, R.drawable.flag_cameroon__cm_
    ), CountryModel(
        "0039", "Canada", "🇨🇦", "CA", "+1", "### ### ####", 17, R.drawable.flag_canada__ca_
    ), CountryModel(
        "0040", "Cape Verde", "🇨🇻", "CV", "+238", "### ####", 17, R.drawable.flag_cabo_verde__cv_
    ), CountryModel(
        "0041",
        "Cayman Islands",
        "🇰🇾",
        "KY",
        "+1345",
        "### ####",
        17,
        R.drawable.flag_cayman_islands__ky_
    ), CountryModel(
        "0042",
        "Central African Rep.",
        "🇨🇫",
        "CF",
        "+236",
        "## ## ## ##",
        17,
        R.drawable.flag_central_african_republic__cf_
    ), CountryModel(
        "0043", "Chad", "🇹🇩", "TD", "+235", "## ## ## ##", 17, R.drawable.flag_chad__td_
    ), CountryModel(
        "0044", "Chile", "🇨🇱", "CL", "+56", "# #### ####", 17, R.drawable.flag_chile__cl_
    ), CountryModel(
        "0045", "China", "🇨🇳", "CN", "+86", "### #### ####", 17, R.drawable.flag_china__cn_
    ), CountryModel(
        "0048", "Colombia", "🇨🇴", "CO", "+57", "### ### ####", 17, R.drawable.flag_colombia__co_
    ), CountryModel(
        "0049", "Comoros", "🇰🇲", "KM", "+269", "### ####", 17, R.drawable.flag_comoros__km_
    ), CountryModel(
        "0050",
        "Congo (Rep.)",
        "🇨🇬",
        "CG",
        "+242",
        "## ### ####",
        17,
        R.drawable.flag_republic_of_the_congo__cg_
    ), CountryModel(
        "0051",
        "Congo (Dem. Rep.)",
        "🇨🇩",
        "CD",
        "+243",
        "## ### ####",
        17,
        R.drawable.flag_democratic_republic_of_the_congo__cd_
    ), CountryModel(
        "0052", "Cook Islands", "🇨🇰", "CK", "+682", "#", 17, R.drawable.flag_cook_islands__ck_
    ), CountryModel(
        "0053", "Costa Rica", "🇨🇷", "CR", "+506", "#### ####", 17, R.drawable.flag_costa_rica__cr_
    ), CountryModel(
        "0054",
        "Cote d'Ivoire",
        "🇨🇮",
        "CI",
        "+225",
        "## ## ## ####",
        17,
        R.drawable.flag_c_te_d_ivoire__ci_
    ), CountryModel(
        "0055", "Croatia", "🇭🇷", "HR", "+385", "## ### ###", 17, R.drawable.flag_croatia__hr_
    ), CountryModel(
        "0056", "Cuba", "🇨🇺", "CU", "+53", "# ### ####", 17, R.drawable.flag_cuba__cu_
    ), CountryModel(
        "0057", "Cyprus", "🇨🇾", "CY", "+357", "#### ####", 17, R.drawable.flag_cyprus__cy_
    ), CountryModel(
        "0058",
        "Czech Republic",
        "🇨🇿",
        "CZ",
        "+420",
        "### ### ###",
        17,
        R.drawable.flag_czech_republic__cz_
    ), CountryModel(
        "0059", "Denmark", "🇩🇰", "DK", "+45", "#### ####", 17, R.drawable.flag_denmark__dk_
    ), CountryModel(
        "006011", "Diego Garcia", "🇮🇴", "IO", "+246", "#", 17, R.drawable.flag_diego_garcia__dg_
    ), CountryModel(
        "0060", "Djibouti", "🇩🇯", "DJ", "+253", "## ## ## ##", 17, R.drawable.flag_djibouti__dj_
    ), CountryModel(
        "0061", "Dominica", "🇩🇲", "DM", "+1767", "### ####", 17, R.drawable.flag_dominica__dm_
    ), CountryModel(

        // TODO: Change the flag
        "0062", "Dominican Rep.", "🇩🇴", "DO", "+1809", "### ####", 17, R.drawable.flag_t_rkiye__tr_
    ), CountryModel(
        "0063", "Ecuador", "🇪🇨", "EC", "+593", "## ### ####", 17, R.drawable.flag_ecuador__ec_
    ), CountryModel(
        "0064", "Egypt", "🇪🇬", "EG", "+20", "## #### ####", 17, R.drawable.flag_egypt__eg_
    ), CountryModel(
        "0065", "El Salvador", "🇸🇻", "SV", "+503", "#### ####", 17, R.drawable.flag_el_salvador__sv_
    ), CountryModel(
        "0066",
        "Equatorial Guinea",
        "🇬🇶",
        "GQ",
        "+240",
        "### ### ###",
        17,
        R.drawable.flag_equatorial_guinea__gq_
    ), CountryModel(
        "0067", "Eritrea", "🇪🇷", "ER", "+291", "# ### ###", 17, R.drawable.flag_eritrea__er_
    ), CountryModel(
        "0068", "Estonia", "🇪🇪", "EE", "+372", "#### ###", 17, R.drawable.flag_estonia__ee_
    ), CountryModel(
        "006811", "Eswatini", "🇸🇿", "SZ", "+268", "#### ####", 17, R.drawable.flag_eswatini__sz_
    ), CountryModel(
        "0069", "Ethiopia", "🇪🇹", "ET", "+251", "## ### ###", 17, R.drawable.flag_ethiopia__et_
    ), CountryModel(
        "0070",
        "Falkland Islands",
        "🇫🇰",
        "FK",
        "+500",
        "#",
        17,
        R.drawable.flag_falkland_islands__fk_
    ), CountryModel(
        "0071",
        "Faroe Islands",
        "🇫🇴",
        "FO",
        "+298",
        "### ###",
        7,
        R.drawable.flag_faroe_islands__fo_
    ), CountryModel(
        "0072", "Fiji", "🇫🇯", "FJ", "+679", "### ####", 17, R.drawable.flag_fiji__fj_
    ), CountryModel(
        "0073", "Finland", "🇫🇮", "FI", "+358", "#", 17, R.drawable.flag_finland__fi_
    ), CountryModel(
        "0074", "France", "🇫🇷", "FR", "+33", "# ## ## ## ##", 17, R.drawable.flag_france__fr_
    ), CountryModel(
        "0075", "French Guiana", "🇬🇫", "GF", "+594", "#", 17, R.drawable.flag_french_guiana__gf_
    ), CountryModel(
        "0076",
        "French Polynesia",
        "🇵🇫",
        "PF",
        "+689",
        "#",
        17,
        R.drawable.flag_french_polynesia__pf_
    ), CountryModel(
        "0078", "Gabon", "🇬🇦", "GA", "+241", "# ## ## ##", 17, R.drawable.flag_gabon__ga_
    ), CountryModel(
        "0079", "Gambia", "🇬🇲", "GM", "+220", "### ####", 17, R.drawable.flag_gambia__gm_
    ), CountryModel(
        "0080", "Georgia", "🇬🇪", "GE", "+995", "### ### ###", 17, R.drawable.flag_georgia__ge_
    ), CountryModel(
        "0081", "Germany", "🇩🇪", "DE", "+49", "#", 17, R.drawable.flag_germany__de_
    ), CountryModel(
        "0082", "Ghana", "🇬🇭", "GH", "+233", "## ### ####", 17, R.drawable.flag_ghana__gh_
    ), CountryModel(
        "0083", "Gibraltar", "🇬🇮", "GI", "+350", "#### ####", 17, R.drawable.flag_gibraltar__gi_
    ), CountryModel(
        "0084", "Greece", "🇬🇷", "GR", "+30", "### ### ####", 17, R.drawable.flag_greece__gr_
    ), CountryModel(
        "0085", "Greenland", "🇬🇱", "GL", "+299", "### ###", 17, R.drawable.flag_greenland__gl_
    ), CountryModel(
        "0086", "Grenada", "🇬🇩", "GD", "+1473", "### ####", 17, R.drawable.flag_grenada__gd_
    ), CountryModel(
        "0087",
        "Guadeloupe",
        "🇬🇵",
        "GP",
        "+590",
        "### ## ## ##",
        17,
        R.drawable.flag_guadeloupe__gp_
    ), CountryModel(
        "0088", "Guam", "🇬🇺", "GU", "+1671", "### ####", 17, R.drawable.flag_guam__gu_
    ), CountryModel(
        "0089", "Guatemala", "🇬🇹", "GT", "+502", "# ### ####", 17, R.drawable.flag_guatemala__gt_
    ), CountryModel(
        "0091", "Guinea", "🇬🇳", "GN", "+224", "### ### ###", 17, R.drawable.flag_guinea__gn_
    ), CountryModel(
        "0092",
        "Guinea-Bissau",
        "🇬🇼",
        "GW",
        "+245",
        "### ## ## ##",
        17,
        R.drawable.flag_guinea_bissau__gw_
    ), CountryModel(
        "0093", "Guyana", "🇬🇾", "GY", "+592", "#", 17, R.drawable.flag_guyana__gy_
    ), CountryModel(
        "0094", "Haiti", "🇭🇹", "HT", "+509", "#### ####", 17, R.drawable.flag_haiti__ht_
    ), CountryModel(
        "0097", "Honduras", "🇭🇳", "HN", "+504", "#### ####", 17, R.drawable.flag_honduras__hn_
    ), CountryModel(
        "0098", "Hong Kong", "🇭🇰", "HK", "+852", "# ### ####", 17, R.drawable.flag_hong_kong__hk_
    ), CountryModel(
        "0099", "Hungary", "🇭🇺", "HU", "+36", "### ### ###", 17, R.drawable.flag_hungary__hu_
    ), CountryModel(
        "0100", "Iceland", "🇮🇸", "IS", "+354", "### ####", 17, R.drawable.flag_iceland__is_
    ), CountryModel(
        "0101", "India", "🇮🇳", "IN", "+91", "##### #####", 17, R.drawable.flag_india__in_
    ), CountryModel(
        "0102", "Indonesia", "🇮🇩", "ID", "+62", "### ######", 17, R.drawable.flag_indonesia__id_
    ), CountryModel(
        "0103", "Iran", "🇮🇷", "IR", "+98", "### ### ####", 17, R.drawable.flag_iran__ir_
    ), CountryModel(
        "0104", "Iraq", "🇮🇶", "IQ", "+964", "### ### ####", 17, R.drawable.flag_iraq__iq_
    ), CountryModel(
        "0105", "Ireland", "🇮🇪", "IE", "+353", "## ### ####", 17, R.drawable.flag_ireland__ie_
    ), CountryModel(
        "0107", "Israel", "🇮🇱", "IL", "+972", "## ### ####", 17, R.drawable.flag_israel__il_
    ), CountryModel(
        "0108", "Italy", "🇮🇹", "IT", "+39", "### ### ####", 17, R.drawable.flag_italy__it_
    ), CountryModel(
        "0109", "Jamaica", "🇯🇲", "JM", "+1876", "### ####", 17, R.drawable.flag_jamaica__jm_
    ), CountryModel(
        "0110", "Japan", "🇯🇵", "JP", "+81", "## #### ####", 17, R.drawable.flag_japan__jp_
    ), CountryModel(
        "0112", "Jordan", "🇯🇴", "JO", "+962", "# #### ####", 17, R.drawable.flag_jordan__jo_
    ), CountryModel(
        "0113", "Kazakhstan", "🇰🇿", "KZ", "+7", "### ### ## ##", 17, R.drawable.flag_kazakhstan__kz_
    ), CountryModel(
        "0114", "Kenya", "🇰🇪", "KE", "+254", "### ### ###", 17, R.drawable.flag_kenya__ke_
    ), CountryModel(
        "0115", "Kiribati", "🇰🇮", "KI", "+686", "#### ####", 17, R.drawable.flag_kiribati__ki_
    ), CountryModel(
        "0116", "North Korea", "🇰🇵", "KP", "+850", "#", 17, R.drawable.flag_north_korea__kp_
    ), CountryModel(
        "0117",
        "South Korea",
        "🇰🇷",
        "KR",
        "+82",
        "## #### ###",
        17,
        R.drawable.flag_south_korea__kr_
    ), CountryModel(
        "0118", "Kosovo", "🇽🇰", "XK", "+383", "#### ####", 17, R.drawable.flag_kosovo__xk_
    ), CountryModel(
        "0119", "Kuwait", "🇰🇼", "KW", "+965", "#### ####", 17, R.drawable.flag_kuwait__kw_
    ), CountryModel(
        "0120", "Kyrgyzstan", "🇰🇬", "KG", "+996", "### ######", 17, R.drawable.flag_kyrgyzstan__kg_
    ), CountryModel(
        "0121", "Laos", "🇱🇦", "LA", "+856", "## ## ### ###", 17, R.drawable.flag_laos__la_
    ), CountryModel(
        "0122", "Latvia", "🇱🇻", "LV", "+371", "### #####", 17, R.drawable.flag_latvia__lv_
    ), CountryModel(
        "0123", "Lebanon", "🇱🇧", "LB", "+961", "#", 17, R.drawable.flag_lebanon__lb_
    ), CountryModel(
        "0124", "Lesotho", "🇱🇸", "LS", "+266", "## ### ###", 17, R.drawable.flag_lesotho__ls_
    ), CountryModel(
        "0125", "Liberia", "🇱🇷", "LR", "+231", "## ### ####", 17, R.drawable.flag_liberia__lr_
    ), CountryModel(
        "0126", "Libya", "🇱🇾", "LY", "+218", "## ### ####", 17, R.drawable.flag_libya__ly_
    ), CountryModel(
        "0127",
        "Liechtenstein",
        "🇱🇮",
        "LI",
        "+423",
        "### ####",
        17,
        R.drawable.flag_liechtenstein__li_
    ), CountryModel(
        "0128", "Lithuania", "🇱🇹", "LT", "+370", "### #####", 17, R.drawable.flag_lithuania__lt_
    ), CountryModel(
        "0129", "Luxembourg", "🇱🇺", "LU", "+352", "### ### ###", 17, R.drawable.flag_luxembourg__lu_
    ), CountryModel(
        "0130", "Macau", "🇲🇴", "MO", "+853", "#### ####", 17, R.drawable.flag_macau__mo_
    ), CountryModel(
        "0131",
        "Macedonia",
        "🇲🇰",
        "MK",
        "+389",
        "## ######",
        17,
        R.drawable.flag_north_macedonia__mk_
    ), CountryModel(
        "0132", "Madagascar", "🇲🇬", "MG", "+261", "## ######", 17, R.drawable.flag_madagascar__mg_
    ), CountryModel(
        "0133", "Malawi", "🇲🇼", "MW", "+265", "## ## ### ##", 17, R.drawable.flag_malawi__mw_
    ), CountryModel(
        "0134", "Malaysia", "🇲🇾", "MY", "+60", "#", 17, R.drawable.flag_malaysia__my_
    ), CountryModel(
        "0135", "Maldives", "🇲🇻", "MV", "+960", "### ####", 17, R.drawable.flag_maldives__mv_
    ), CountryModel(
        "0136", "Mali", "🇲🇱", "ML", "+223", "#### ####", 17, R.drawable.flag_mali__ml_
    ), CountryModel(
        "0137", "Malta", "🇲🇹", "MT", "+356", "## ## ## ##", 17, R.drawable.flag_malta__mt_
    ), CountryModel(
        "0138",
        "Marshall Islands",
        "🇲🇭",
        "MH",
        "+692",
        "#",
        17,
        R.drawable.flag_marshall_islands__mh_
    ), CountryModel(
        "0139", "Martinique", "🇲🇶", "MQ", "+596", "#", 17, R.drawable.flag_martinique__mq_
    ), CountryModel(
        "0140", "Mauritania", "🇲🇷", "MR", "+222", "#### ####", 17, R.drawable.flag_mauritania__mr_
    ), CountryModel(
        "0141", "Mauritius", "🇲🇺", "MU", "+230", "#### ####", 17, R.drawable.flag_mauritius__mu_
    ), CountryModel(
        "0143", "Mexico", "🇲🇽", "MX", "+52", "#", 17, R.drawable.flag_mexico__mx_
    ), CountryModel(
        "0144",
        "Micronesia",
        "🇫🇲",
        "FM",
        "+691",
        "#",
        17,
        R.drawable.flag_federated_states_of_micronesia__fm_
    ), CountryModel(
        "0145", "Moldova", "🇲🇩", "MD", "+373", "## ### ###", 17, R.drawable.flag_moldova__md_
    ), CountryModel(
        "0146", "Monaco", "🇲🇨", "MC", "+377", "#### ####", 17, R.drawable.flag_monaco__mc_
    ), CountryModel(
        "0147", "Mongolia", "🇲🇳", "MN", "+976", "## ## ####", 17, R.drawable.flag_mongolia__mn_
    ), CountryModel(
        "0148", "Montenegro", "🇲🇪", "ME", "+382", "#", 17, R.drawable.flag_montenegro__me_
    ), CountryModel(
        "0149", "Montserrat", "🇲🇸", "MS", "+1664", "### ####", 17, R.drawable.flag_montserrat__ms_
    ), CountryModel(
        "0150", "Morocco", "🇲🇦", "MA", "+212", "## ### ####", 17, R.drawable.flag_morocco__ma_
    ), CountryModel(
        "0151", "Mozambique", "🇲🇿", "MZ", "+258", "## ### ####", 17, R.drawable.flag_mozambique__mz_
    ), CountryModel(
        "0152", "Myanmar", "🇲🇲", "MM", "+95", "#", 17, R.drawable.flag_myanmar__mm_
    ), CountryModel(
        "0153", "Namibia", "🇳🇦", "NA", "+264", "## ### ####", 17, R.drawable.flag_namibia__na_
    ), CountryModel(
        "0154", "Nauru", "🇳🇷", "NR", "+674", "#", 17, R.drawable.flag_nauru__nr_
    ), CountryModel(
        "0155", "Nepal", "🇳🇵", "NP", "+977", "## #### ####", 17, R.drawable.flag_nepal__np_
    ), CountryModel(
        "0156",
        "Netherlands",
        "🇳🇱",
        "NL",
        "+31",
        "# ## ## ## ##",
        17,
        R.drawable.flag_netherlands__nl_
    ), CountryModel(
        "0158", "New Caledonia", "🇳🇨", "NC", "+687", "#", 17, R.drawable.flag_new_caledonia__nc_
    ), CountryModel(
        "0159", "New Zealand", "🇳🇿", "NZ", "+64", "#### ####", 17, R.drawable.flag_new_zealand__nz_
    ), CountryModel(
        "0160", "Nicaragua", "🇳🇮", "NI", "+505", "#### ####", 17, R.drawable.flag_nicaragua__ni_
    ), CountryModel(
        "0161", "Niger", "🇳🇪", "NE", "+227", "## ## ## ##", 17, R.drawable.flag_niger__ne_
    ), CountryModel(
        "0162", "Nigeria", "🇳🇬", "NG", "+234", "## #### ####", 17, R.drawable.flag_nigeria__ng_
    ), CountryModel(
        "0163", "Niue", "🇳🇺", "NU", "+683", "#", 17, R.drawable.flag_niue__nu_
    ), CountryModel(
        "0164", "Norfolk Island", "🇳🇫", "NF", "+672", "#", 17, R.drawable.flag_norfolk_island__nf_
    ), CountryModel(
        "0165",
        "Northern Mariana Islands",
        "🇲🇵",
        "MP",
        "+1670",
        "### ####",
        17,
        R.drawable.flag_northern_mariana_islands__mp_
    ), CountryModel(
        "0166", "Norway", "🇳🇴", "NO", "+47", "### ## ###", 17, R.drawable.flag_norway__no_
    ), CountryModel(
        "0167", "Oman", "🇴🇲", "OM", "+968", "#### ####", 17, R.drawable.flag_oman__om_
    ), CountryModel(
        "0168", "Pakistan", "🇵🇰", "PK", "+92", "### ### ####", 17, R.drawable.flag_pakistan__pk_
    ), CountryModel(
        "0169", "Palau", "🇵🇼", "PW", "+680", "#", 17, R.drawable.flag_palau__pw_
    ), CountryModel(
        "0170",
        "Palestine",
        "🇵🇸",
        "PS",
        "+970",
        "### ## ####",
        17,
        R.drawable.flag_state_of_palestine__ps_
    ), CountryModel(
        "0171", "Panama", "🇵🇦", "PA", "+507", "#### ####", 17, R.drawable.flag_panama__pa_
    ), CountryModel(
        "0172",
        "Papua New Guinea",
        "🇵🇬",
        "PG",
        "+675",
        "#",
        17,
        R.drawable.flag_papua_new_guinea__pg_
    ), CountryModel(
        "0173", "Paraguay", "🇵🇾", "PY", "+595", "### ### ###", 17, R.drawable.flag_paraguay__py_
    ), CountryModel(
        "0174", "Peru", "🇵🇪", "PE", "+51", "### ### ###", 17, R.drawable.flag_peru__pe_
    ), CountryModel(
        "0175",
        "Philippines",
        "🇵🇭",
        "PH",
        "+63",
        "### ### ####",
        17,
        R.drawable.flag_philippines__ph_
    ), CountryModel(
        "0177", "Poland", "🇵🇱", "PL", "+48", "### ### ###", 17, R.drawable.flag_poland__pl_
    ), CountryModel(
        "0178", "Portugal", "🇵🇹", "PT", "+351", "### ### ###", 17, R.drawable.flag_portugal__pt_
    ), CountryModel(
        "0179", "Puerto Rico", "🇵🇷", "PR", "+1939", "### ####", 17, R.drawable.flag_puerto_rico__pr_
    ), CountryModel(
        "0180", "Qatar", "🇶🇦", "QA", "+974", "## ### ###", 17, R.drawable.flag_qatar__qa_
    ), CountryModel(
        "0181", "Romania", "🇷🇴", "RO", "+40", "### ### ###", 17, R.drawable.flag_romania__ro_
    ), CountryModel(
        "0182", "Russia", "🇷🇺", "RU", "+7", "### ### ####", 17, R.drawable.flag_russia__ru_
    ), CountryModel(
        "0183", "Rwanda", "🇷🇼", "RW", "+250", "### ### ###", 17, R.drawable.flag_rwanda__rw_
    ), CountryModel(
        "0184", "Reunion", "🇷🇪", "RE", "+262", "### ### ###", 17, R.drawable.flag_r_union__re_
    ), CountryModel(
        "0186",
        "Saint Helena",
        "🇸🇭",
        "SH",
        "+290",
        "#",
        17,
        R.drawable.flag_saint_helena_ascension_and_tristan_da_cunha__sh_
    ), CountryModel(
        "0187",
        "Saint Kitts & Nevis",
        "🇰🇳",
        "KN",
        "+1869",
        "### ####",
        17,
        R.drawable.flag_saint_kitts_and_nevis__kn_
    ), CountryModel(
        "0188", "Saint Lucia", "🇱🇨", "LC", "+1758", "### ####", 17, R.drawable.flag_saint_lucia__lc_
    ), CountryModel(
        "0190",
        "Saint Pierre & Miquelon",
        "🇵🇲",
        "PM",
        "+508",
        "#",
        17,
        R.drawable.flag_saint_pierre_and_miquelon__pm_
    ), CountryModel(
        "0191",
        "Saint Vincent & the Grenadines",
        "🇻🇨",
        "VC",
        "+1784",
        "### ####",
        17,
        R.drawable.flag_saint_vincent_and_the_grenadines__vc_
    ), CountryModel(
        "0192", "Samoa", "🇼🇸", "WS", "+685", "#", 17, R.drawable.flag_samoa__ws_
    ), CountryModel(
        "0193", "San Marino", "🇸🇲", "SM", "+378", "#", 17, R.drawable.flag_san_marino__sm_
    ), CountryModel(
        "0194",
        "Sao Tome & Principe",
        "🇸🇹",
        "ST",
        "+239",
        "## #####",
        17,
        R.drawable.flag_sao_tome_and_principe__st_
    ), CountryModel(
        "0195",
        "Saudi Arabia",
        "🇸🇦",
        "SA",
        "+966",
        "## ### ####",
        17,
        R.drawable.flag_saudi_arabia__sa_
    ), CountryModel(
        "0196", "Senegal", "🇸🇳", "SN", "+221", "## ### ####", 17, R.drawable.flag_senegal__sn_
    ), CountryModel(
        "0197", "Serbia", "🇷🇸", "RS", "+381", "## ### ###", 17, R.drawable.flag_serbia__rs_
    ), CountryModel(
        "0198", "Seychelles", "🇸🇨", "SC", "+248", "# ## ## ##", 17, R.drawable.flag_seychelles__sc_
    ), CountryModel(
        "0199",
        "Sierra Leone",
        "🇸🇱",
        "SL",
        "+232",
        "## ### ###",
        17,
        R.drawable.flag_sierra_leone__sl_
    ), CountryModel(
        "0200", "Singapore", "🇸🇬", "SG", "+65", "#### ####", 17, R.drawable.flag_singapore__sg_
    ), CountryModel(
        "0201", "Slovakia", "🇸🇰", "SK", "+421", "### ### ###", 17, R.drawable.flag_slovakia__sk_
    ), CountryModel(
        "0202", "Slovenia", "🇸🇮", "SI", "+386", "## ### ###", 17, R.drawable.flag_slovenia__si_
    ), CountryModel(
        "0203", "Solomon Islands", "🇸🇧", "SB", "+677", "#", 17, R.drawable.flag_solomon_islands__sb_
    ), CountryModel(
        "0204", "Somalia", "🇸🇴", "SO", "+252", "## ### ###", 17, R.drawable.flag_somalia__so_
    ), CountryModel(
        "0205",
        "South Africa",
        "🇿🇦",
        "ZA",
        "+27",
        "## ### ####",
        17,
        R.drawable.flag_south_africa__za_
    ), CountryModel(
        "0206",
        "South Sudan",
        "🇸🇸",
        "SS",
        "+211",
        "## ### ####",
        17,
        R.drawable.flag_south_sudan__ss_
    ), CountryModel(
        "0208", "Spain", "🇪🇸", "ES", "+34", "### ### ###", 17, R.drawable.flag_spain__es_
    ), CountryModel(
        "0209", "Sri Lanka", "🇱🇰", "LK", "+94", "## ### ####", 17, R.drawable.flag_sri_lanka__lk_
    ), CountryModel(
        "0210", "Sudan", "🇸🇩", "SD", "+249", "## ### ####", 17, R.drawable.flag_sudan__sd_
    ), CountryModel(
        "0211", "Suriname", "🇸🇷", "SR", "+597", "### ####", 17, R.drawable.flag_suriname__sr_
    ), CountryModel(
        "0214", "Sweden", "🇸🇪", "SE", "+46", "## ### ####", 17, R.drawable.flag_sweden__se_
    ), CountryModel(
        "0215",
        "Switzerland",
        "🇨🇭",
        "CH",
        "+41",
        "## ### ####",
        17,
        R.drawable.flag_switzerland__ch_
    ), CountryModel(
        "0216", "Syria", "🇸🇾", "SY", "+963", "### ### ###", 17, R.drawable.flag_syria__sy_
    ), CountryModel(
        "0217", "Taiwan", "🇹🇼", "TW", "+886", "### ### ###", 17, R.drawable.flag_taiwan__tw_
    ), CountryModel(
        "0218", "Tajikistan", "🇹🇯", "TJ", "+992", "## ### ####", 17, R.drawable.flag_tajikistan__tj_
    ), CountryModel(
        "0219", "Tanzania", "🇹🇿", "TZ", "+255", "## ### ####", 17, R.drawable.flag_tanzania__tz_
    ), CountryModel(
        "0220", "Thailand", "🇹🇭", "TH", "+66", "# #### ####", 17, R.drawable.flag_thailand__th_
    ), CountryModel(
        "0221", "Timor-Leste", "🇹🇱", "TL", "+670", "#", 17, R.drawable.flag_timor_leste__tl_
    ), CountryModel(
        "0222", "Togo", "🇹🇬", "TG", "+228", "## ### ###", 17, R.drawable.flag_togo__tg_
    ), CountryModel(
        "0223", "Tokelau", "🇹🇰", "TK", "+690", "#", 17, R.drawable.flag_tokelau__tk_
    ), CountryModel(
        "0224", "Tonga", "🇹🇴", "TO", "+676", "#", 17, R.drawable.flag_tonga__to_
    ), CountryModel(
        "0225",
        "Trinidad & Tobago",
        "🇹🇹",
        "TT",
        "+1868",
        "### ####",
        17,
        R.drawable.flag_trinidad_and_tobago__tt_
    ), CountryModel(
        "0226", "Tunisia", "🇹🇳", "TN", "+216", "## ### ###", 17, R.drawable.flag_tunisia__tn_
    ), CountryModel(
        "0227", "Turkey", "🇹🇷", "TR", "+90", "### ### ####", 17, R.drawable.flag_t_rkiye__tr_
    ), CountryModel(
        "0228",
        "Turkmenistan",
        "🇹🇲",
        "TM",
        "+993",
        "## ######",
        17,
        R.drawable.flag_turkmenistan__tm_
    ), CountryModel(
        "0229",
        "Turks & Caicos Islands",
        "🇹🇨",
        "TC",
        "+1649",
        "### ####",
        17,
        R.drawable.flag_turks_and_caicos_islands__tc_
    ), CountryModel(
        //TODO: CHange the Flag here
        "0230", "Tuvalu", "🇹🇻", "TV", "+688", "#", 17, R.drawable.flag_virgin_islands__u_s____vi_
    ), CountryModel(
        "0231", "Uganda", "🇺🇬", "UG", "+256", "## ### ####", 17, R.drawable.flag_uganda__ug_
    ), CountryModel(
        "0232", "Ukraine", "🇺🇦", "UA", "+380", "## ### ## ##", 17, R.drawable.flag_ukraine__ua_
    ), CountryModel(
        "0233",
        "United Arab Emirates",
        "🇦🇪",
        "AE",
        "+971",
        "## ### ####",
        17,
        R.drawable.flag_united_arab_emirates__ae_
    ), CountryModel(
        "0234",
        "United Kingdom",
        "🇬🇧",
        "GB",
        "+44",
        "#### ######",
        17,
        R.drawable.flag_united_kingdom__gb_
    ), CountryModel(
        "0235",
        "USA",
        "🇺🇸",
        "US",
        "+1",
        "### ### ####",
        17,
        R.drawable.flag_united_states_of_america__us_
    ), CountryModel(
        "0236", "Uruguay", "🇺🇾", "UY", "+598", "# ### ####", 17, R.drawable.flag_uruguay__uy_
    ), CountryModel(
        "0237",
        "Uzbekistan",
        "🇺🇿",
        "UZ",
        "+998",
        "## ### ## ##",
        17,
        R.drawable.flag_uzbekistan__uz_
    ), CountryModel(
        "0238", "Vanuatu", "🇻🇺", "VU", "+678", "#", 17, R.drawable.flag_vanuatu__vu_
    ), CountryModel(
        "0239", "Venezuela", "🇻🇪", "VE", "+58", "### ### ####", 17, R.drawable.flag_venezuela__ve_
    ), CountryModel(
        "0240", "Vietnam", "🇻🇳", "VN", "+84", "#", 17, R.drawable.flag_vietnam__vn_
    ), CountryModel(
        "0241",
        "Virgin Islands, British",
        "🇻🇬",
        "VG",
        "+1284",
        "### ####",
        17,
        R.drawable.flag_virgin_islands__british___vg_
    ), CountryModel(
        "0242",
        "Virgin Islands, U.S.",
        "🇻🇮",
        "VI",
        "+1340",
        "### ####",
        17,
        R.drawable.flag_virgin_islands__u_s____vi_
    ), CountryModel(
        "0243",
        "Wallis & Futuna",
        "🇼🇫",
        "WF",
        "+681",
        "#",
        17,
        R.drawable.flag_wallis_and_futuna__wf_
    ), CountryModel(
        "0244", "Yemen", "🇾🇪", "YE", "+967", "### ### ###", 17, R.drawable.flag_yemen__ye_
    ), CountryModel(
        "0245", "Zambia", "🇿🇲", "ZM", "+260", "## ### ####", 17, R.drawable.flag_zambia__zm_
    ), CountryModel(
        "0246", "Zimbabwe", "🇿🇼", "ZW", "+263", "## ### ####", 17, R.drawable.flag_zimbabwe__zw_
    )

)

val countryHashes = HashMap<String, CountryModel>()

fun getCountryByCode(code: String): CountryModel? {
    return countryList.find { it.code == code }
}

fun getCountryByName(name: String): CountryModel? {
    return countryList.find { it.name.lowercase() == name.lowercase() }
}

fun getCountryByDialingCode(dialingCode: String): CountryModel? {
    return countryList.find { it.dialCode == dialingCode }
}

fun getCountryCode(): String {
    val tm = App.baseApplication.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    val code = tm.networkCountryIso.uppercase()
    if (code.isEmpty()) {
        return App.baseApplication.resources.configuration.locales.get(0).country.uppercase()
    }
    return code
}
