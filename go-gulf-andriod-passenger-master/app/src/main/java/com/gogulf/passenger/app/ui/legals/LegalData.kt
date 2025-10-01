package com.gogulf.passenger.app.ui.legals

object LegalData {
    private var mList = ArrayList<LegalModels>()


    fun setData(): ArrayList<LegalModels> {
        mList.clear()
        addData(
            "Terms and Conditions",
            "",
            "Below are the Terms and Conditions of Slyyk"
        )
        addData(
            "",
            "Acknowledgment and acceptance of terms and conditions:",
            "This app/website is owned and operated by Slyyk Australia Pty Ltd (ACN: 628 466 900) trading as Slyyk. By accessing, browsing or using this app/website, you agree to these terms, conditions, and disclaimers as amended from time to time (“Terms & Conditions”) and acknowledge and agree that you have read and understood these Terms and Conditions. The SLYYK App allows users to request and access transportation services from registered and accredited/licensed third party taxi and or private car hire transportation service providers. If accessing, downloading and utilizing the application, you are bound by the terms of use as set out below. If you refuse to accept the following terms of use, you must not proceed in utilizing the application and forfeit any protections or securities the terms and conditions may imply or express. Effective from 01/01/2019 the following apply: You must register a personal credit card within the Slyyk app to book any form of requested transport. Meaning of words:  In these Terms of Use:\n" +
                    "•\tSLYYK App refers to the software application “SLYYK” which allows users to request and access transportation services from registered and licensed third party accredited taxi and private car operators by way of providing a platform to connect drivers and riders. The app is available to download from the Apple store and Google Play;\n" +
                    "•\tWebsite refers to www.slyyk.com, its related web pages located under a single domain name and any variation or tailored version of the website created for your use;\n" +
                    "•\tPlatform An application, especially as downloaded by a user to a mobile device.\n" +
                    "•\tStripe Eftpos Payment Service means the payment gateway service operated by Strike payment system\n" +
                    "•\tProviders refer to accredited/registered and licensed third party taxi and private hire car transportation service providers/operators;\n" +
                    "•\tContent includes any information or data submitted by you through the SLYYK App and all information and data made available to you by us through our SLYYK App and services;\n" +
                    "•\tIntellectual Property means, whether registered or not, all copyright, designs and industrial designs, circuit layouts, trademarks, service marks and commercial names and designations, trade secrets, know-how confidential information, patents, invention and discoveries, literary artistic and scientific works, inventions in all fields of human endeavour, and other results of intellectual activity in the industrial, commercial, scientific, literary or artistic fields;\n" +
                    "•\tThe notice includes a disclaimer, a disclosure or other statement, the fact of observing or paying attention to something, a consent, and terms, and conditions;\n" +
                    "•\tYou refers to you/member/operator/driver as the user of the SLYYK App.\n"
        )
        addData(
            "",
            "Users and Accounts:",
            "•\tTo access and use our services, you will have to download the SLYYK App onto a compatible mobile device and create a new user account, by providing certain personal information, including your name, address, mobile phone number, as well as a username, password and payment details;\n" +
                    "•\tTo create a user account with SLYYK you must be at least 18 years of age or otherwise have valid parental consent to utilize our services if you are underage. As such, if you create an account you will be deemed to have received parental consent if under the age of 18;\n" +
                    "•\tTo use our services, you must be located in Australia.\n" +
                    "•\tYou are responsible for maintaining the security and confidentiality of your user credentials and password ensuring that these details remain accurate and up-to-date. See our privacy policy for further information on how we will use your personal information;\n" +
                    "•\tYou are responsible for any and all activities that occur under your account. You agree to notify us immediately of any unauthorized use of your account or any other breach of security;\n" +
                    "•\tWe will not be liable for any loss that you may incur as a result of someone else using your account, either with or without your knowledge. You will be held liable for losses incurred by us or another party due to someone else having breached your account whether with or without your consent;\n" +
                    "•\tYou may not use anyone else’s account at any time, nor assign or transfer your account to any other person;\n" +
                    "•\tYou agree to maintain and update your user information, data and password as required to keep it accurate, current, and complete;\n" +
                    "•\tYou agree that we may store and use the user information and data you provide us (including payment card information) for use in maintaining your accounts and for billing fees to your payment card;\n" +
                    "•\tYou agree that we have no responsibility or liability for the deletion or failure to store any Content maintained or transmitted by the SLYYK App or via our Services.\n" +
                    "•\tSLYYK App is live, sign up and get yourself familiar with the features, you don’t have to be affiliated with the SLYYK network to join at this stage.\n" +
                    "•\tTime to time you will need to update the SLYYK App to receive the new features, you can do this by going to the Apple IOS & GOOGLE play store.\n" +
                    "•\tDrivers choose the discounts they want to offer to passengers, drivers can offer 10%, 20%, 30% and 40% on the estimated/fixed taxi fare, you can add extras if the trip took longer or extras needed to be added informing the passengers of the extras. E.g. vomiting fee or leaving a mess in the vehicle fees are \$120 in Victoria, airport parking, tolls, and entry fees to certain theme parks or snow trips.\n" +
                    "•\tDriver should offer 40% to receive the first booking depending on how many drivers on the same discount offer, passengers will always request for the highest discount (40%), 30% discount is around the same pricing as Ride Share and 40% will be cheaper and BETTER then Ride Share, the higher discount you give to the passenger the more times they will book with you and use the SLYYK App as their preferred app.\n" +
                    "•\tDrivers that offer the highest DISCOUNT will always be the FIRST to receive the bookings, then those who offer less like 30%, 20%, and 10% and last in line with no discount offers at all.\n" +
                    "•\tThe premium fee of \$11 is added by the passenger when requesting a taxi pick on the SLYYK App e.g. passenger might request a premium vehicle or in peak times can include a premium fee of \$11 so keep an eye out.\n" +
                    "•\tThe minimum fare is \$5, the taxi fare should be set greater than or equal to \$5 or payment won’t be complete. (Payment provider cannot process payments under \$5) (You can manually enter \$5 for the payment to proceed; passenger needs to be informed that is mentioned in the terms and conditions).\n" +
                    "•\tInvoices are raised at the end of the trip by the driver and payment is accepted/made by the passenger and finalised, the notification will be sent to the driver directly and paid into his nominated account minus the extras e.g. \$1.10 Levy fee, 90c Management fee, 5% credit card surcharge, \$11 Premium fee and any other fees and charges incurred along the way on the trip, all charges and fees are subject to change without notice.\n" +
                    "•\tProviders pay .90 cents management fee for each booking completed.\n" +
                    "•\tBanning the use of the SLYYK App, if there is continues resubmitting of work from the driver and continues cancelation from the passenger, management has the right to ban users from using the SLYYK App without notice.\n" +
                    "•\tPassengers must enter a valid credit card to be active on the SLYYK App.\n" +
                    "•\tDriver Vehicle and Driver Accreditation must be active at all times, always be “Active” and ready to take any booking.\n"
        )
        addData(
            "",
            "SLYYK Services:",
            "You acknowledge and agree that:\n" +
                    "•\tThe SLYYK App is a platform to connect passengers wishing to book personal transport services and deliver the ability to request transportation services from Providers within a certain area. The Provider may or may not accept to transport you from your selected pick-up location to your desired final destination;\n" +
                    "•\twe do not guarantee or warrant that Providers will be available when you need them or that Providers will be able to provide you their services within any certain/ period of time, any level of quality or to your satisfaction;\n" +
                    "•\twhen you receive transportation services from a Provider you enter into a contract directly with that Provider for those services and SLYYK is not a party to that contract;\n" +
                    "•\twe are not responsible or liable to you in any way for the actions, behavior, omissions or any damage or loss caused by Providers when providing or failing to provide you transportation services and\n" +
                    "•\tany services requested through the SLYYK App may or may not warrant a fee as determined within the app;\n"
        )
        addData(
            "",
            "Through the SLYYK App, SLYYK provides you with:",
            "•\tinformation about transportation services offered by registered and licensed Providers;\n" +
                    "•\tinformation about Providers to enable you to request transportation services from Providers;\n" +
                    "•\tthe ability to request transportation services from available Providers who may be located near you;\n" +
                    "•\twhen your request is accepted, access to transportation services from registered and licensed Providers;\n" +
                    "•\tthe ability to rate your experience and Provider; and\n" +
                    "•\tthe ability to pay a Provider a fee for their transportation services, through the Stripe Eftpos Payment Service.\n" +
                    "•\tOnce you have entered both your pick-up and final drop-off destinations, selected a Provider, and confirmed the request, the Provider is able to locate you through the Global Positioning System (GPS) receiver on your mobile phone device. When the Provider accepts your trip request, the App allows you to track the location and progress of the Provider’s journey in real-time.\n" +
                    "•\tYou are provided with certain information about your driver and Provider through the App, which may include the registration of the vehicle, the SLYYK Provider rating, a photograph of the relevant driver, and the ability to contact the driver directly within the application.\n" +
                    "•\tAt the end of the trip, you will receive a final fare payable on the SLYYK App, which may include additional fees if you have verbally modified your trip with the Provider during the course of your journey.\n" +
                    "•\tOn completion, both you and the Provider will be asked to use the SLYYK Provider rating to evaluate the overall experience of the trip, factoring in qualities such as cleanliness, comfort, safe driving, navigational skill and behavior of the driver.\n" +
                    "•\tIf you have any concerns, queries, discrepancies or complaint regarding your fare or SLYYK experience you must do so within twenty-four hours of your booking otherwise you may be charged the final fare on the nominated credit card registered on your SLYYK App without the ability to claim a refund.\n"
        )
        addData(
            "",
            "The user of the SLYYK platform, obligations:",
            "•\tYou agree and warrant that you will provide us information that is true, complete and accurate.\n" +
                    "•\tIt is your responsibility to verify the identity of the Provider, confirm the booking and be available at the requested pick-up location.\n" +
                    "•\tIf you no longer wish to proceed with your booking, you must cancel it as soon as practicable, which may incur a cancellation fee.\n" +
                    "•\tWhen using the Services, you and any accompanying passengers must treat Providers with respect and not cause damage to their vehicles or engage in any unlawful, threatening, harassing, obscene, abusive behavior or activity or any behavior which we reasonably believe could damage our reputation.\n" +
                    "•\tYou and any accompanying passengers must comply with all applicable laws as well as any terms and conditions of Providers’ transportation services, including minimum standards of behavior and intoxication.\n" +
                    "•\tYou agree that any damage caused by you or other passengers traveling with you in a Provider’s vehicle will incur additional charges, including any cleaning costs due to your or other passengers’ behavior.\n" +
                    "•\tIf your mobile phone is stolen or lost, you must immediately notify SLYYK and request your SLYYK account be canceled or suspended in order to prevent any unauthorized use of the SLYYK App or Services.\n" +
                    "•\tYou must not harm or attempt to harm the App or its functions or Services in any way, including but not limited to:\n" +
                    "•\tremoving or altering any copyright, trademark, logo or other proprietary notices or labels from any part of the App;\n" +
                    "•\tmodifying, adapting, encrypting, decompiling, tampering or reverse engineering all or any part of the App;\n" +
                    "•\treproducing or making derivative works based on the App;\n" +
                    "•\tnever allowing others to use your mobile to pay for trips you did not travel in, do not give your mobile to others.\n" +
                    "•\tdistributing, licensing, leasing, selling, reselling or otherwise exploiting the Services and the App; and\n" +
                    "•\tattempting to gain unauthorized access to or impair any part of the Services, App and related systems or networks.\n"
        )
        addData(
            "",
            "Permitted use and SLYYK Platform:",
            ": You agree to not use the SLYYK Platform:\n" +
                    "•\tFor any purpose that is unlawful or prohibited by these Terms of Use;\n" +
                    "•\tin any manner that could damage, disable, overburden, or impair our server, or the network(s) connected to our server, or interfere with any other party’s use and enjoyment of the SLYYK App;\n" +
                    "•\tto attempt to gain unauthorized access to any service, other accounts, computer systems or networks connected to our server or services through hacking, password mining or any other means;\n" +
                    "•\tto attempt to obtain any materials or information through any means not intentionally made available through our SLYYK App;\n" +
                    "•\tto transmit or otherwise make available any Content that is unlawful, harmful, threatening, abusive, harassing, tortious, defamatory, vulgar, obscene, libelous, invasive of another’s privacy, hateful, or racially, ethnically or otherwise objectionable;\n" +
                    "•\tto impersonate any person or entity;\n" +
                    "•\tto transmit or otherwise make available any Content that you do not have a right to make available under any law or which infringes any patent, trademark, trade secret, copyright or other proprietary rights of any party;\n" +
                    "•\tto transmit or otherwise make available any unsolicited or unauthorized advertising, promotional materials, “junk mail,” “spam,” “chain letters,” “pyramid schemes,” or any other form of solicitation unless expressly authorized to do so;\n" +
                    "•\tto stalk or otherwise harass another; or\n" +
                    "•\tto collect or store personal data about other users.\n"
        )
        addData(
            "",
            "We have the right (but not the obligation) in our sole discretion to:",
            "•\trefuse the transmission of any Content via the SLYYK App;\n" +
                    "•\trefuse access to or use of the SLYYK App;\n" +
                    "•\tremove any Content that violates these Terms of Use or is otherwise deemed by us to be objectionable;\n" +
                    "•\tpreserve or disclose Content if required to do so by law or in the good faith belief that such preservation or disclosure is reasonably necessary to: (a) comply with legal process; (b) enforce the Terms of Use; (c) respond to claims that any Content violates the rights of third-parties; or (d) protect our rights, property or the personal safety of our staff, other users and the public.\n" +
                    "•\tAs a condition of your use of our SLYYK App, you authorize us to include you or your organization’s name in our published list of users.\n" +
                    "•\tThe information in our SLYYK App, the Services and these Terms of Use have been prepared in accordance with the laws of the State of Victoria, and the Commonwealth of Australia. The SLYYK App and our services may not comply with the laws of any other State or country.\n"
        )
        addData(
            "",
            "Payment Methods, Pricing, and Fees:",
            "•\tThe SLYYK App is free to use, but you may be subject to data or other mobile phone network costs.\n" +
                    "•\tWhen you use the SLYYK App to obtain a transportation service from a Provider, you must pay the Provider the final fee levied for these services including a government service levy if applicable in your state or other extras from your journey. Payment may be made either:\n" +
                    "•\tdirectly to the Provider, by way of cash, credit card or EFTPOS using the Provider’s payment facilities in their vehicle; or\n" +
                    "•\tProviders pay .90 cents SLYYK management fee for each booking completed.\n" +
                    "•\tthrough the SLYYK App, by credit card via the Stripe Payment System.\n" +
                    "•\tWhen you elect to pay by a registered credit card on the SLYYK App, you acknowledge that your credit card details are valid and are verified by the Stripe Payment Service subject to the terms & conditions of use by Live Group.\n" +
                    "•\tMetered fare and fees are regulated by governmental bodies and may exclude toll-road fees and surcharge fees for non-cash payments of fares, which is not at the discretion of SLYYK, nor the Provider.\n" +
                    "•\tYou acknowledge and agree that the “Estimate Fare” provided on the SLYYK App when confirming a booking is merely an estimate and the final fee payable to the Provider is dependent on other variables such as time, distance, route driven, tolls and traffic or other extras which may increase the fare payable by you.\n" +
                    "•\tYou acknowledge and agree that when specifically booking a private car transportation service, the “Set Rate” indicated on the App in the booking process is the fee that will be charged to you once your booking request has been accepted. This fee is inclusive of the discount or tip and subject to any additional charges that the Provider may validly impose on you, including tolls. Set Rates are calculated independently by SLYYK and are comparative to those charged in the private car transportation service industry.\n" +
                    "•\tWhen accepting your trip request, providers can offer discounts from 10% to 40% of the journey.\n" +
                    "•\tYou agree that certain times of the day or night providers can charge a premium service fee through the SLYYK App.\n" +
                    "•\tYou understand you may incur a booking fee charge for a booking accepted by a Provider, however you will be made aware of this charge prior to your trip and can elect whether you wish to proceed on the SLYYK App.\n" +
                    "•\tIf you cancel your booking after the Provider has accepted or you are not available at the pick-up location nominated by you (including if you accept the services of another driver in the vicinity in error), a cancellation fee of up to 50% of the total fare, but no more than \$12 may be charged to the credit card registered on your SLYYK App.\n" +
                    "•\tYou agree that the Provider may impose additional fees e.g. bodily fluids and liquids or in relation to any damage to their vehicle caused by your negligence or behavior or the passengers traveling with you.\n" +
                    "•\tYou acknowledge that your credit card must have sufficient funds and full payment of the journey/levied fee must be made and that any attempts to evade payment of fare may be reported by us or the Provider to the relevant authorities.\n" +
                    "•\tAll amounts payable for the Services provided by SLYYK are inclusive of Goods and Services Tax (GST). Where not marked inclusive of GST, an amount payable must be increased to include the amount of GST payable.\n" +
                    "•\tYou acknowledge that all card payments attract a 5% service fee (transaction surcharge) added at the end of every trip the credit card surcharge is applied on top of your transaction. The transaction fee is subject to change.\n" +
                    "•\t You acknowledge and agree to authorize to pay DRIVERS using the Stripe Payment Service.\n"
        )
        addData(
            "",
            "Limitation of liability:",
            "•\tSLYYK, its related bodies corporate, any of their directors, officers, employees, or shareholders will in no way be liable for any direct, indirect, incidental, special or consequential damages (including in negligence), resulting from your use or your inability to use this app/website. Nor will it be liable for any reliance on any information or advice contained on this website or for the cost of procurement of substitute products or services or resulting from any products or services purchased or obtained or messages received or transactions entered into through this app/website or resulting from unauthorized access to or alteration of your transmissions or data or of any information contained on your computer system or on this website, including but not limited to, damages for loss of profits, loss of use, loss of data or other intangible, even if SLYYK has been advised as to the possibility of such damages.\n" +
                    "•\tWhere any Act of Parliament implies in these Terms and Conditions any term, condition or warranty, and that Act avoids or prohibits provisions in a contract excluding or modifying the application of or exercise of, or liability under such term, condition or warranty, such term, condition or warranty shall be deemed to be included in these Terms and Conditions, however, the liability of SLYYK for any breach of such term, condition or warranty shall be limited to the minimum permitted by the Act of Parliament.\n" +
                    "•\tSubject to our obligations under the Non-Excludable Provisions, neither we nor any of our employees, agents or officers are liable for any direct or indirect loss, any costs, charges or expenses you incur in connection with or arising from: information published, displayed or available through our SLYYK App or provided via our Services;\n" +
                    "•\tany action is taken, failure to act, a decision made or reliance by you on the basis of the Content and the data and information in the SLYYK App provided through the Services;\n" +
                    "•\tany modification, suspension or discontinuance of the SLYYK App or our Services;\n" +
                    "•\tany errors or delays in the Content, or for any actions you or third parties may take in reliance on it;\n" +
                    "•\tyour use of any services or offers provided by any individual, firm or company or Provider referred by us to you via the SLYYK App or our Services. Nothing in these Terms of Use is or should be interpreted as an attempt to modify, limit or exclude any right or remedy, or any guarantee, term, condition, warranty, undertaking, inducement or representation, implied or imposed by legislation which cannot be lawfully modified, limited or excluded. This may include the Australian Consumer Law, which contains guarantees that protect consumers who buy goods and services in certain circumstances.\n" +
                    "•\tTo the extent permitted by law, we exclude the application of all other guarantees, terms, conditions, warranties undertakings, inducements and representations express or implied by statute or otherwise.\n" +
                    "•\tYou agree that you do not rely on any guarantee, term, condition, warranty, undertaking, inducement or representation made by us or on our behalf which is not expressly stated in these Terms of Use.\n" +
                    "•\tSubject to our obligations under the Non-Excludable Provisions, our maximum aggregate liability to you in respect of any one claim or series of connected claims under these Terms of Use or in connection with its subject matter, whether arising in or for breach of contract, negligence or other tort, breach of statutory duty, or under an indemnity or otherwise is limited to \$50.\n" +
                    "•\tSubject to our obligations under the Non-Excludable Provisions, we will not be liable to you for any incidental, special, punitive, consequential or indirect loss or damages, lost profits, lost data, personal injury or property damage related to, in connection with, or otherwise resulting from any use of the SLYYK App or our Services.\n"
        )
        addData(
            "",
            "Indemnity:",
            "•\tYou agree to indemnify, and hold us harmless from any claims, actions, losses or demands relating to or arising out of your use of the SLYYK App and Services, including:\n" +
                    "•\tany Content you submit through the SLYYK App;\n" +
                    "•\tyour use or misuse of the SLYYK App or Services; any violation of these Terms of Use by you or passengers that travel with you;\n" +
                    "•\tany violation of third party rights, including Providers by you or passengers that travel with you; and\n" +
                    "•\tany unlawful, wilful or negligent act or omission by you or passengers that travel with you.\n"
        )
        addData(
            "",
            "Provider Liability:",
            "You acknowledge and agree that:\n" +
                    "•\tWhen using the SLYYK App and Services, you will not be covered under any insurance provided or held by SLYYK;\n" +
                    "•\tSLYYK does not guarantee that Providers will have their own insurance to cover any loss, damage or claim from passengers; and\n" +
                    "•\tSLYYK does not guarantee that Providers have a legally registered vehicle that is in roadworthy condition or licensed to provide transportation services.\n" +
                    "•\tYou acknowledge that when you access and use our services, you enter directly into a contract with the relevant Provider for their Services. We are not responsible for the behavior, actions or inactions of Providers you use through the SLYYK App.\n" +
                    "•\tSLYYK will not be held liable nor responsible for any injury, loss or damage experienced by a passenger when using the Services. You confirm and accept this when using our Services and must bring any claim or action against an individual Provider and Notley.\n"
        )
        addData(
            "",
            "Disputes:",
            "•\tIn the event that there is a dispute or complaint between you and a Provider in relation to the transportation services provided, then we will use our best efforts to investigate the dispute and assist you and the Provider to resolve the dispute.\n" +
                    "•\tIt is your obligation to notify us of any dispute promptly and within 48 hours of the relevant incident. You may do so by contacting SLYYK:\n" +
                    "•\tvia email to support@slyyk.com, or\n" +
                    "•\tTelephone 1300 12 13 14\n"
        )
        addData(
            "",
            "SLYYK App security:",
            "•\tWe do not guarantee that information transmitted over the internet and/or through the SLYYK App is totally secure. Therefore, when you send us information you do so at your own risk. Once we have received it, we take reasonable steps to keep the information secure while it is in our own systems, but we do not guarantee that it is secure.\n" +
                    "•\tYour use of our SLYYK App and our Services is at your own risk. We do not guarantee that our software application is free from viruses, or that access to our SLYYK App or Services will be uninterrupted. You should, therefore, ensure that your device is protected from viruses and any other interference that could damage your device.\n"
        )
        addData(
            "",
            "Information about you & your privacy:",
            "•\tWhen you have downloaded and utilised the SLYYK App, we may collect personal information about you, including your credit card or payment details. Ordinarily, we will explain the purposes for which we collect that information when it is collected. As a general rule, we only collect such information which is necessary for us to provide our Service to you or to maintain our relationship with you.\n" +
                    "•\tYour personal information will not be shared by us with any other person or organization, except our related bodies corporate, licensees, drivers, and suppliers, so that we may adopt an integrated approach to our customers by improving the app platform. We will not disclose your personal information to foreign recipients.\n" +
                    "•\tSLYYK PRIVACY POLICY sets out how we collect and deal with personal information generally, including our use of cookies on your device. Our PRIVACY POLICY contains information about how you may access your personal information that is held by us and seek the correction of that information, as well as information about how you may submit a complaint about any breach of the Australian Privacy Principles.  If you have any queries about the collection, storage, use or disclosure of your personal information by contacting SLYYK at support@slyyk.com.\n" +
                    "•\tBy downloading and using the SLYYK App you acknowledge and agree with our Privacy Policy, and consent for us to collect and disclose your personal information as necessary to provide you a better transport platform.\n"
        )
        addData(
            "",
            "Third-party websites",
            "•\twww.slyyk.com may contain links to app/websites maintained by other companies and organizations. Slyyk does not make any representation as to the accuracy of the information contained on those websites and will not accept any responsibility for the accuracy, ownership or any other aspect of the information contained on those websites.\n" +
                    "•\twww.slyyk.com may also contain third party advertisements (including banner ads and referral buttons) which contain embedded hyperlinks to websites operated by third parties. All third-party advertising is paid for by the relevant third-party advertisers and the placement of third party advertisements on the website does not constitute a recommendation or endorsement by SLYYK of the products or services advertised. The third-party advertiser is solely responsible to you for any representations or offers made by it. To the extent permitted by law, we are not responsible or liable for and give no warranty in respect of, any third-party website, application or the goods and services (including software) offered by a third party or any information appearing in any product or service we may offer. SLYYK may receive payments from third parties in relation to goods or services supplied or received as a result of users and third parties accessing any links to third-party applications or websites contained in our SLYYK App or website.\n"
        )
        addData(
            "",
            "Virus warning:",
            "•\tSLYYK does not represent that any information (including any file) obtained from or through the website is free from computer viruses or other faults or defects. It is your responsibility to scan any such information for computer viruses. SLYYK will not be liable to you or to any other person for any loss or damage whether direct, indirect, consequential or economic, however, caused and whether by negligence or otherwise, which may result directly or indirectly from any such information. To the extent of any applicable law that cannot be excluded imposes any liability on SLYYK, that liability shall be limited to the cost of re-supplying that information."
        )
        addData(
            "",
            "Intellectual Property:",
            "•\tYou acknowledge all Content and Intellectual Property held, located within and related to our SLYYK App and Services is the property of SLYYK and protected by Intellectual Property law. Nothing in these Terms of Use conveys or vests to you any interests or ownership in SLYYK’s Intellectual Property or Content.\n" +
                    "•\tBy accessing and using the SLYYK App and our Services, you agree not to redistribute or resell any Intellectual Property obtained from the SLYYK App and Services, without our prior written consent.\n" +
                    "•\tOther than for the purposes and subject to the conditions prescribed under the Copyright Act 1968 (Cth), you must not reproduce, upload to a third party, link to, frame, store in a retrieval system or transmit any part of it without our prior written consent.\n" +
                    "•\tOur SLYYK App includes registered trademarks owned by us (or our licensors). You must not use any of these trademarks in any way without our prior written consent.\n" +
                    "•\tYou agree that you will not do or cause to be done any act or thing that may impair any of SLYYK’s Intellectual Property rights in connection with the SLYYK App or Services."
        )
        addData(
            "",
            "Termination and Modification of Service:",
            "•\tWe may in our sole discretion immediately suspend, terminate or limit your access to the SLYYK App platform if:\n" +
                    "•\twe deem that you are in breach of the Terms of Use or our Privacy Policy;\n" +
                    "•\tyou have not paid any fee payable under these Terms of Use on the date and time they are due; or\n" +
                    "•\twe deem that your use of the SLYYK App and Services warrants termination of your access.\n" +
                    "•\tWe will notify you of such suspension, termination or limitation by email within five (5) business days. You agree that we will not be liable to you or any third-party for any termination of your access to the SLYYK App or the Services.\n" +
                    "•\tWe reserve the right at any time and from time to time to modify or discontinue, temporarily or permanently, the SLYYK App and Services (or any part thereof) with or without notice. You agree that we will not be liable to you or to any third party for any modification, suspension or discontinuance of our services."
        )
        addData(
            "",
            "Notice:",
            "•\tSLYYK may give Notice under these Terms of Use by means of notice on the SLYYK App or website, SMS or by email."
        )
        addData(
            "",
            "Governing Law: Jurisdiction, Severability and Waiver:",
            "•\tThese Terms and Conditions shall be governed by and construed in accordance with the laws of the State of Victoria and the Commonwealth of Australia.\n" +
                    "•\tIf any provision of these Terms is held illegal or unenforceable, then such illegality or unenforceability shall not affect the remaining provisions of these Terms which shall remain in full force and effect.\n" +
                    "•\tYou and SLYYK agree to submit to the exclusive jurisdiction of the courts of the State of Victoria, Australia or any Federal Court sitting in Victoria, Australia.\n" +
                    "•\tSLYYK’s failure to exercise or enforce any rights or provisions of these Terms and Conditions shall not constitute a waiver of such right or provision unless acknowledged and agreed to by SLYYK in writing.\n" +
                    "•\tAny failure or delay by SLYYK in exercising any right, power or privilege available to us will not operate as a waiver of that power or right.\n" +
                    "•\tIf any provisions of these Terms of Use become void, voidable or unenforceable then those provisions are deemed to be severed and the remaining provisions will continue to have full force and effect."
        )
        addData(
            "",
            "Changes to these Terms of Use:",
            "•\tThis statement sets out our current Terms of Use. It replaces any other Terms of Use which we have previously issued.\n" +
                    "•\tThese Terms of Use may be updated by us at any time. You are responsible for checking these Terms of Use regularly, so you are aware of any updates that we have made to them. If you do not agree with any of the updated Terms of Use, you may stop using the SLYYK App at any time and de-install the SLYYK App from your device.\n" +
                    "•\tWe suggest you periodically review our Terms of Use for any changes.\n" +
                    "•\tAll information within the SLYYK App and features are subject to change without notice."
        )
        addData(
            "",
            "Disclaimer:",
            "•\tThe information contained on this app/website is provided by SLYYK in good faith on an “as is” basis and solely at your own risk. The information is believed to be accurate and current at the date the information was placed on this website but SLYYK expressly disclaims any liability for any incorrect information or prices published on this website or for the lack availability of any product.\n" +
                    "•\tNeither SLYYK, its related bodies corporate, any of their directors, officers, employees or shareholders makes any representation or warranty as to the reliability, accuracy or completeness of any of the information or ideas contained on this website including in relation to any products or services.SLYYK, its related bodies corporate, any of their directors, officers, employees or shareholders will accept any responsibility arising in any way including negligence for errors in, or omissions from, the information contained on this website. To the fullest extent permitted by applicable law, SLYYK disclaims all representations and warranties, express or implied, including but not limited to, implied warranties.\n" +
                    "•\tSlyyk makes no warranty that this app/website, any information or ideas contained on this app/website or any products or services advertised on this website will meet your requirements, or that the website will be uninterrupted, timely, secure or error-free."
        )
        addData(
            "",
            "Contacting us:",
            "If you have any questions regarding these Terms of Use please contact us at support@slyyk.com"
        )


        return mList

    }

    private fun addData(mainTitle: String, subTitle: String, content: String) {
        mList.add(LegalModels(mainTitle, subTitle, content))
    }
}