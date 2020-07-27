package com.o2o.action.server.app;

import com.google.actions.api.ActionRequest;
import com.google.actions.api.ActionResponse;
import com.google.actions.api.DialogflowApp;
import com.google.actions.api.ForIntent;
import com.google.actions.api.response.ResponseBuilder;
import com.google.actions.api.response.helperintent.SelectionList;
import com.google.api.services.actions_fulfillment.v2.model.*;
import com.o2o.action.server.util.CommonUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class internApp extends DialogflowApp {

	/**
	 * welcome intent
	 *
	 * @param request
	 * @return
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	@ForIntent("Default Welcome Intent")
	public ActionResponse defaultWelcome(ActionRequest request) throws ExecutionException, InterruptedException {
		ResponseBuilder responseBuilder = getResponseBuilder(request);
		Map<String, Object> data = responseBuilder.getConversationData();

		data.clear();

		List<String> suggestions = new ArrayList<String>();
		SimpleResponse simpleResponse = new SimpleResponse(); //말풍선
		BasicCard basicCard = new BasicCard();

		simpleResponse.setTextToSpeech("반가워요, 어떤 드라마를 좋아하세요?") //소리
				.setDisplayText("취향에 맞는 드라마를 추천해드릴게요.") //글 표시
		;

		basicCard
				.setTitle("재밌는 드라마 추천")
				.setImage(new Image().setUrl("https://actions.o2o.kr/devsvr1/image/home.png")
						.setAccessibilityText("home"))
				.setImageDisplayOptions("CROPPED")
		;

		SimpleResponse simpleResponse2 = new SimpleResponse();
		simpleResponse2.setTextToSpeech("장르, 배우, 작가 중 하나를 선택해주세요");

		suggestions.add("장르");
		suggestions.add("배우");
		suggestions.add("작가");

		responseBuilder.add(simpleResponse);
		responseBuilder.add(basicCard);
		responseBuilder.add(simpleResponse2);
		responseBuilder.addSuggestions(suggestions.toArray(new String[suggestions.size()]));

		return responseBuilder.build();
	}

	/**
	 * 배우,장르 혹은 작가 중 하나를 선택하면 나오는 또 다른 선택
	 *
	 * @param request
	 * @return
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	@ForIntent("Choice")
	public ActionResponse choice(ActionRequest request) throws ExecutionException, InterruptedException {
		ResponseBuilder responseBuilder = getResponseBuilder(request);
		Map<String, Object> data = responseBuilder.getConversationData();

		data.clear();

		List<String> suggestions = new ArrayList<String>();
		SimpleResponse simpleResponse = new SimpleResponse();
		BasicCard basicCard = new BasicCard();

		String choose = CommonUtil.makeSafeString(request.getParameter("choose"));

		if (choose.equals("장르")) { //장르 선택 (문제 발생)
			simpleResponse
					.setTextToSpeech("어떤 장르의 드라마를 불러올까요?");
			basicCard
					.setImage(
							new Image()
									.setUrl("https://actions.o2o.kr/devsvr1/image/genre.jpg")
					)
					.setImageDisplayOptions("CROPPED");
			suggestions.add("범죄");
			suggestions.add("판타지");
			suggestions.add("로맨스");
		} else if (choose.equals("배우")) { //배우 선택
			simpleResponse
					.setTextToSpeech("어떤 배우를 좋아하세요?");
			basicCard
					.setImage(
							new Image()
									.setUrl("https://actions.o2o.kr/devsvr1/image/actor.png")
					);
			suggestions.add("성동일");
			suggestions.add("서현진");
			suggestions.add("이지은(아이유)");
		} else if (choose.equals("작가")) { //작가 선택
			simpleResponse
					.setTextToSpeech("어떤 작가의 작품을 좋아하시나요?");
			basicCard
					.setImage(
							new Image()
									.setUrl("https://actions.o2o.kr/devsvr1/image/author.jpg")
					);
			suggestions.add("김은희");
			suggestions.add("김은숙");
			suggestions.add("노희경");
		}

		responseBuilder.add(simpleResponse);
		responseBuilder.add(basicCard);
		responseBuilder.addSuggestions(suggestions.toArray(new String[suggestions.size()]));

		return responseBuilder.build();
	}

	/**
	 * 선택한 장르,배우,작가에 맞는 드라마 목록을 생성하는 인텐트
	 *
	 * @param request
	 * @return
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	@ForIntent("Chosen Drama List")
	public ActionResponse chosenDramaList(ActionRequest request) throws ExecutionException, InterruptedException {
		ResponseBuilder responseBuilder = getResponseBuilder(request);
		Map<String, Object> data = responseBuilder.getConversationData();

		data.clear();

		SimpleResponse simpleResponse = new SimpleResponse();
		SelectionList selectionList = new SelectionList();

		final String actor = CommonUtil.makeSafeString(request.getParameter("actor")); //countries : (현재) 미국, 영국, 스페인, 한국
		final String genre = CommonUtil.makeSafeString(request.getParameter("genre")); //genre : (현재) 범죄, 판타지, 로맨틱코미디, 10대
		final String author = CommonUtil.makeSafeString(request.getParameter("author"));

		if (genre.equals("") && author.equals("")) { //actor 선택
			switch (actor) {
				case "성동일":
					selectionList
							.setTitle("성동일 배우의 드라마 목록")
							.setItems(
									Arrays.asList(
											new ListSelectListItem()
													.setTitle("라이브")
													.setDescription("배우 : 정유미, 배성우, 성동일 등 (2018)")
													.setImage(
															new Image()
																	.setUrl("https://actions.o2o.kr/devsvr1/image/drama1.jpg")
																	.setAccessibilityText("라이브")
													)
													.setOptionInfo(
															new OptionInfo()
																	.setKey("1")
													),
											new ListSelectListItem()
													.setTitle("응답하라 1988")
													.setDescription("배우 : 류준열, 성동일, 이일화 등 (2015)")
													.setImage(
															new Image()
																	.setUrl("https://actions.o2o.kr/devsvr1/image/drama2.jpg")
																	.setAccessibilityText("응답하라 1988")
													)
													.setOptionInfo(
															new OptionInfo()
																	.setKey("2")
													),
											new ListSelectListItem()
													.setTitle("괜찮아, 사랑이야")
													.setDescription("배우 : 조인성, 공효진, 성동일 등 (2014)")
													.setImage(
															new Image()
																	.setUrl("https://actions.o2o.kr/devsvr1/image/drama3.jpg")
																	.setAccessibilityText("괜찮아, 사랑이야")
													)
													.setOptionInfo(
															new OptionInfo()
																	.setKey("3")
													)
									)
							);
					break;
				case "서현진":
					selectionList
							.setTitle("서현진 배우의 드라마 목록")
							.setItems(
									Arrays.asList(
											new ListSelectListItem()
													.setTitle("뷰티 인사이드")
													.setDescription("배우 : 서현진, 이민기, 안재현 등 (2018)")
													.setImage(
															new Image()
																	.setUrl("https://actions.o2o.kr/devsvr1/image/drama4.jpg")
																	.setAccessibilityText("뷰티 인사이드")
													)
													.setOptionInfo(
															new OptionInfo()
																	.setKey("4")
													),
											new ListSelectListItem()
													.setTitle("또! 오해영")
													.setDescription("배우 : 서현진, 에릭, 전혜빈 등 (2016)")
													.setImage(
															new Image()
																	.setUrl("https://actions.o2o.kr/devsvr1/image/drama5.jpg")
																	.setAccessibilityText("또! 오해영")
													)
													.setOptionInfo(
															new OptionInfo()
																	.setKey("5")
													),
											new ListSelectListItem()
													.setTitle("식샤를 합시다2")
													.setDescription("배우 : 윤두준, 서현진 등 (2015)")
													.setImage(
															new Image()
																	.setUrl("https://actions.o2o.kr/devsvr1/image/drama6.jpg")
																	.setAccessibilityText("식샤를 합니디2")
													)
													.setOptionInfo(
															new OptionInfo()
																	.setKey("6")
													)
									)
							);
					break;
				case "이지은":
				case "아이유":
					selectionList
							.setTitle("이지은(아이유) 배우의 드라마 목록")
							.setItems(
									Arrays.asList(
											new ListSelectListItem()
													.setTitle("달의 연인 - 보보경심 려")
													.setDescription("배우 : 이준기, 이지은, 강하늘 등 (2016)")
													.setImage(
															new Image()
																	.setUrl("https://actions.o2o.kr/devsvr1/image/drama7.jpg")
																	.setAccessibilityText("달의 연인 - 보보경심 려")
													)
													.setOptionInfo(
															new OptionInfo()
																	.setKey("7")
													),
											new ListSelectListItem()
													.setTitle("페르소나")
													.setDescription("배우 : 이지은, 배두나 등 (2018)")
													.setImage(
															new Image()
																	.setUrl("https://actions.o2o.kr/devsvr1/image/drama8.jpg")
																	.setAccessibilityText("페르소나")
													)
													.setOptionInfo(
															new OptionInfo()
																	.setKey("8")
													),
											new ListSelectListItem()
													.setTitle("나의 아저씨")
													.setDescription("배우 : 이지은, 이선균 등 (2018)")
													.setImage(
															new Image()
																	.setUrl("https://actions.o2o.kr/devsvr1/image/drama9.jpg")
																	.setAccessibilityText("나의 아저씨")
													)
													.setOptionInfo(
															new OptionInfo()
																	.setKey("9")
													)
									)
							);
					break;
			}

		} else if (actor.equals("") && author.equals("")) { //genre 선택
			switch (genre) {
				case "로맨스":
					selectionList
							.setTitle("로맨스 드라마")
							.setItems(
									Arrays.asList(
											new ListSelectListItem()
													.setTitle("도깨비")
													.setDescription("배우 : 공유, 이동욱 등 (2016)")
													.setImage(
															new Image()
																	.setUrl("https://actions.o2o.kr/devsvr1/image/drama10.jpg")
																	.setAccessibilityText("도깨비")
													)
													.setOptionInfo(
															new OptionInfo()
																	.setKey("10")
													),
											new ListSelectListItem()
													.setTitle("멜로가 체질")
													.setDescription("배우 : 천우희, 전여빈 등 (2019)")
													.setImage(
															new Image()
																	.setUrl("https://actions.o2o.kr/devsvr1/image/drama11.jpg")
																	.setAccessibilityText("멜로가 체질")
													)
													.setOptionInfo(
															new OptionInfo()
																	.setKey("11")
													),
                                            new ListSelectListItem()
                                                    .setTitle("뷰티 인사이드")
                                                    .setDescription("배우 : 서현진, 이민기 등 (2018)")
                                                    .setImage(
                                                            new Image()
                                                                    .setUrl("https://actions.o2o.kr/devsvr1/image/drama4.jpg")
                                                                    .setAccessibilityText("뷰티 인사이드")
                                                    )
                                                    .setOptionInfo(
                                                            new OptionInfo()
                                                                    .setKey("4")
                                                    )
									)
							);
					break;
				case "판타지":
					selectionList
							.setTitle("판타지 드라마 목록")
							.setItems(
									Arrays.asList(
                                            new ListSelectListItem()
                                                    .setTitle("도깨비")
                                                    .setDescription("배우 : 공유, 이동욱 등 (2016)")
                                                    .setImage(
                                                            new Image()
                                                                    .setUrl("https://actions.o2o.kr/devsvr1/image/drama10.jpg")
                                                                    .setAccessibilityText("도깨비")
                                                    )
                                                    .setOptionInfo(
                                                            new OptionInfo()
                                                                    .setKey("10")
                                                    ),
											new ListSelectListItem()
													.setTitle("알함브라 궁전의 추억")
													.setDescription("배우 : 박신혜, 현빈 등 (2018)")
													.setImage(
															new Image()
																	.setUrl("https://actions.o2o.kr/devsvr1/image/drama12.jpg")
																	.setAccessibilityText("알함브라 궁전의 추억")
													)
													.setOptionInfo(
															new OptionInfo()
																	.setKey("12")
													),
											new ListSelectListItem()
													.setTitle("어비스")
													.setDescription("배우 : 박보영, 안효섭 등 (2019)")
													.setImage(
															new Image()
																	.setUrl("https://actions.o2o.kr/devsvr1/image/drama13.jpg")
																	.setAccessibilityText("어비스")
													)
													.setOptionInfo(
															new OptionInfo()
																	.setKey("13")
													)
									)
							);
					break;
				case "범죄":
					selectionList
							.setTitle("범죄 드라마 목록")
							.setItems(
									Arrays.asList(
                                            new ListSelectListItem()
                                                    .setTitle("시그널")
                                                    .setDescription("배우 : 김혜수, 조진웅 등 (2016)")
                                                    .setImage(
                                                            new Image()
                                                                    .setUrl("https://actions.o2o.kr/devsvr1/image/drama14.jpg")
                                                                    .setAccessibilityText("시그널")
                                                    )
                                                    .setOptionInfo(
                                                            new OptionInfo()
                                                                    .setKey("14")
                                                    ),
											new ListSelectListItem()
													.setTitle("인간수업")
													.setDescription("배우 : 김동희, 박주현 등 (2020)")
													.setImage(
															new Image()
																	.setUrl("https://actions.o2o.kr/devsvr1/image/drama15.jpg")
																	.setAccessibilityText("인간수업")
													)
													.setOptionInfo(
															new OptionInfo()
																	.setKey("15")
													),
											new ListSelectListItem()
													.setTitle("보이스")
													.setDescription("배우 : 이하나, 장혁 등 (2017)")
													.setImage(
															new Image()
																	.setUrl("https://actions.o2o.kr/devsvr1/image/drama16.jpg")
																	.setAccessibilityText("보이스")
													)
													.setOptionInfo(
															new OptionInfo()
																	.setKey("16")
													)
									)
							);
					break;
			}
		} else if (actor.equals("") && genre.equals("")) { //author 선택
			switch (author) {
				case "김은희":
					selectionList
							.setTitle("김은희 작가의 드라마 목록")
							.setItems(
									Arrays.asList(
											new ListSelectListItem()
													.setTitle("시그널")
													.setDescription("배우 : 김혜수, 조진웅 등 (2016)")
													.setImage(
															new Image()
																	.setUrl("https://actions.o2o.kr/devsvr1/image/drama14.jpg")
																	.setAccessibilityText("시그널")
													)
													.setOptionInfo(
															new OptionInfo()
																	.setKey("14")
													),
											new ListSelectListItem()
													.setTitle("킹덤")
													.setDescription("배우 : 주지훈, 류승룡 등 (2019)")
													.setImage(
															new Image()
																	.setUrl("https://actions.o2o.kr/devsvr1/image/drama17.jpg")
																	.setAccessibilityText("킹덤")
													)
													.setOptionInfo(
															new OptionInfo()
																	.setKey("17")
													)
									)
							);
					break;
				case "김은숙":
					selectionList
							.setTitle("김은숙 작가의 드라마 목록")
							.setItems(
									Arrays.asList(
											new ListSelectListItem()
													.setTitle("미스터 션샤인")
													.setDescription("배우 : 이병헌, 김태리 등 (2018)")
													.setImage(
															new Image()
																	.setUrl("https://actions.o2o.kr/devsvr1/image/drama18.jpg")
																	.setAccessibilityText("미스터 션샤인")
													)
													.setOptionInfo(
															new OptionInfo()
																	.setKey("18")
													),
											new ListSelectListItem()
													.setTitle("더 킹 - 영원의 군주")
													.setDescription("배우 : 김고은, 이민호 등 (2020)")
													.setImage(
															new Image()
																	.setUrl("https://actions.o2o.kr/devsvr1/image/drama19.jpg")
																	.setAccessibilityText("더 킹 - 영원의 군주")
													)
													.setOptionInfo(
															new OptionInfo()
																	.setKey("19")
													),
											new ListSelectListItem()
													.setTitle("도깨비")
													.setDescription("배우 : 공유, 이동욱 등 (2016)")
													.setImage(
															new Image()
																	.setUrl("https://actions.o2o.kr/devsvr1/image/drama10.jpg")
																	.setAccessibilityText("도깨비")
													)
													.setOptionInfo(
															new OptionInfo()
																	.setKey("10")
													)
									)
							);
					break;
				case "노희경":
					selectionList
							.setTitle("노희경 작가의 드라마 목록")
							.setItems(
									Arrays.asList(
											new ListSelectListItem()
													.setTitle("디어 마이 프렌즈")
													.setDescription("배우 : 고현정 ,고두심 등 (2016)")
													.setImage(
															new Image()
																	.setUrl("https://actions.o2o.kr/devsvr1/image/drama20.jpg")
																	.setAccessibilityText("디어 마이 프렌즈")
													)
													.setOptionInfo(
															new OptionInfo()
																	.setKey("20")
													),
                                            new ListSelectListItem()
                                                    .setTitle("괜찮아, 사랑이야")
                                                    .setDescription("배우 : 조인성, 공효진, 성동일 등 (2014)")
                                                    .setImage(
                                                            new Image()
                                                                    .setUrl("https://actions.o2o.kr/devsvr1/image/drama3.jpg")
                                                                    .setAccessibilityText("괜찮아, 사랑이야")
                                                    )
                                                    .setOptionInfo(
                                                            new OptionInfo()
                                                                    .setKey("3")
                                                    ),
											new ListSelectListItem()
													.setTitle("라이브")
													.setDescription("배우 : 정유미, 배성우 등 (2018)")
													.setImage(
															new Image()
																	.setUrl("https://actions.o2o.kr/devsvr1/image/drama1.jpg")
																	.setAccessibilityText("라이브")
													)
													.setOptionInfo(
															new OptionInfo()
																	.setKey("1")
													)
									)
							);
					break;
			}
		}

		simpleResponse.setTextToSpeech("보고싶은 드라마를 골라주세요.")
				.setDisplayText("어떤 드라마를 보고싶으신가요?");

		responseBuilder.add(selectionList);
		responseBuilder.add(simpleResponse);

		return responseBuilder.build();
	}

	/**
	 * 선택한 드라마에 대한 설명을 출력하는 인텐트
	 *
	 * @param request
	 * @return
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	@ForIntent("Drama Description")
	public ActionResponse dramaDescription(ActionRequest request) throws ExecutionException, InterruptedException {
		ResponseBuilder responseBuilder = getResponseBuilder(request);
		Map<String, Object> data = responseBuilder.getConversationData();

		data.clear();

		SimpleResponse simpleResponse2 = new SimpleResponse();
		BasicCard basicCard = new BasicCard();

		String drama = CommonUtil.makeSafeString(request.getParameter("drama"));

		String selectedItem = request.getSelectedOption();
		String netflixId = "";

		if (selectedItem.equals("1") || drama.equals("라이브")) {
			simpleResponse2
					.setTextToSpeech("네, 라이브에 대해 알려드릴게요 " + drama);
			String synopsis = "공권력의 상징 대한민국 경찰. 하지만 이들도 알고 보면 우리와 닮은 이웃일 뿐. 제복의 무게를 견디며 오늘도 정의를 위해 출동이다! 특별하고도 평범한 삶을 위하여.";
			String actors = "정유미, 이광수, 성동일";
			basicCard
					.setTitle("라이브")
					.setImage(new Image().setUrl("https://actions.o2o.kr/devsvr1/image/drama1.jpg")
							.setAccessibilityText("라이브"))
					.setFormattedText(" ● 줄거리 : " + synopsis + " ● 배우 : " + actors);
			data.put("netflixId","80214523");
		} else if (selectedItem.equals("2") || drama.equals("응답하라1988")) {
			simpleResponse2
					.setTextToSpeech("네, 응답하라 1988에 대해 알려드릴게요");
			String synopsis = "쌍팔년도 쌍문동, 한 골목 다섯 가족의 왁자지껄 코믹 가족극";
			String actors = "성동일, 이일화, 라미란";
			basicCard
					.setTitle("응답하라 1988")
					.setImage(new Image().setUrl("https://actions.o2o.kr/devsvr1/image/drama2.jpg")
							.setAccessibilityText("응답하라 1988"))
                    .setFormattedText(" ● 줄거리 : " + synopsis + " ● 배우 : " + actors);
			data.put("netflixId","80188351");
		} else if (selectedItem.equals("3") || drama.equals("괜찮아사랑이야")) {
			simpleResponse2
					.setTextToSpeech(drama + "선택한 \'괜찮아, 사랑이야\'에요");
			String synopsis = "인기 추리소설 작가이자 라디오 DJ 재열과 대학병원 정신과 교수 의사 해수. 만나기만 하면 티격태격하던 두 사람이 한집에 살게 되면서 변하기 시작한다.";
			String actors = "조인성, 공효진, 성동일";
			basicCard
					.setTitle("괜찮아, 사랑이야")
					.setImage(new Image().setUrl("https://actions.o2o.kr/devsvr1/image/drama3.jpg")
							.setAccessibilityText("괜찮아, 사랑이야"))
                    .setFormattedText(" ● 줄거리 : " + synopsis + " ● 배우 : " + actors);
			data.put("netflixId","80031632");
		} else if (selectedItem.equals("5") || drama.equals("또오해영")) {
			simpleResponse2
					.setTextToSpeech("네, 또! 오해영에 대해 알려드릴게요");
			String synopsis = "이름만 달랐어도 인생이 좀 나아졌을까? 학창 시절, 예쁘고 잘난 동명이인때문에 온갖 수난을 겪으며 살아온 여자 오해영. 이제는 만날 일 없다고 생각했지만 웬걸. 다시 나타난 예쁜 그녀가 해영의 삶을 또 한 번 망쳐놓을 줄이야!";
			String actors = "에릭, 서현진, 전해빈";
			basicCard
					.setTitle("또! 오해영")
					.setImage(new Image().setUrl("https://actions.o2o.kr/devsvr1/image/drama5.jpg")
							.setAccessibilityText("또! 오해영"))
                    .setFormattedText(" ● 줄거리 : " + synopsis + " ● 배우 : " + actors);
			data.put("netflixId","81077044");
		} else if (selectedItem.equals("4") || drama.equals("뷰티인사이드")) {
			simpleResponse2
					.setTextToSpeech("네, 뷰티 인사이드에 대해 알려드릴게요");
			String synopsis = "이번엔 누구? 한 달에 일주일, 다른 사람으로 사는 여자. 이 사람 누구? 열두 달 매일, 다른 사람 얼굴을 못 알아보는 남자. 남모를 속사정이 있는 남녀가 만났다. 서로의 비밀스러운 세계로 발을 디딘 둘의 로맨스는 어떤 모습일까.";
			String actors = "서현진, 이민기, 이다희";
			basicCard
					.setTitle("뷰티 인사이드")
					.setImage(new Image().setUrl("https://actions.o2o.kr/devsvr1/image/drama4.jpg")
							.setAccessibilityText("뷰티 인사이드"))
                    .setFormattedText(" ● 줄거리 : " + synopsis + " ● 배우 : " + actors);
			data.put("netflixId","81029990");
		} else if (selectedItem.equals("6") || drama.equals("식샤를합시다2")) {
			simpleResponse2
					.setTextToSpeech("네, 식샤를 합시다2에 대해 알려드릴게요");
			String synopsis = "구대영의 적수가 나타났다! 맛집 블로거 식샤님 '구대영'과 그를 원수로 기억하는 '1일1식 다이어트' 4년차 프리랜서 작가 '백수지', 초식남 공무원 '이상우'. 입맛 다른 '세종 빌라' 1인 가구들의 매콤하게 맛있는 드라마";
			String actors = "윤두준, 서현진, 권율";
			basicCard
					.setTitle("식샤를 합시다2")
					.setImage(new Image().setUrl("https://actions.o2o.kr/devsvr1/image/drama6.jpg")
							.setAccessibilityText("식샤를 합시다2"))
                    .setFormattedText(" ● 줄거리 : " + synopsis + " ● 배우 : " + actors);
			data.put("netflixId","80183878");
		} else if (selectedItem.equals("7") || drama.equals("달의연인")) {
			simpleResponse2
					.setTextToSpeech("네, 달의 연인 - 보보경심 려에 대해 알려드릴게요");
			String synopsis = "달그림자가 태양을 검게 물들인 날. 상처 입은 짐승 같은 사내, 4황자 '왕소'와 21세기 여인 '고하진'의 영혼이 미끄러져 들어간 고려 소녀 '해수'가 천 년의 시공간을 초월해 만난다.";
			String actors = "이준기, 이지은, 강하늘";
			basicCard
					.setTitle("달의 연인 - 보보경심 려")
					.setImage(new Image().setUrl("https://actions.o2o.kr/devsvr1/image/drama7.jpg")
							.setAccessibilityText("달의 연인  - 보보경심 려"))
                    .setFormattedText(" ● 줄거리 : " + synopsis + " ● 배우 : " + actors);
			data.put("netflixId","80156759");
		} else if (selectedItem.equals("8") || drama.equals("페르소나")) {
			simpleResponse2
					.setTextToSpeech("네, 페르소나에 대해 알려드릴게요");
			String synopsis = "이경미, 임필성, 전고운, 김종관 4명의 감독이 페르소나 이지은을 각기 다른 시선으로 풀어낸 총 4편의 오리지널 시리즈";
			String actors = "이지은, 배두나, 박해수";
			basicCard
					.setTitle("페르소나")
					.setImage(new Image().setUrl("https://actions.o2o.kr/devsvr1/image/drama8.jpg")
							.setAccessibilityText("페르소나"))
                    .setFormattedText(" ● 줄거리 : " + synopsis + " ● 배우 : " + actors);
			data.put("netflixId","81044884");
		} else if (selectedItem.equals("9") || drama.equals("나의아저씨")) {
			simpleResponse2
					.setTextToSpeech("네, 나의 아저씨에 대해 알려드릴게요");
			String synopsis = "삶의 무게를 버티며 살아가는 아저씨 삼 형제와 거칠게 살아온 한 여성이 서로를 통해 삶을 치유하게 되는 이야기";
			String actors = "이선균, 이지은, 고두심";
			basicCard
					.setTitle("나의 아저씨")
					.setImage(new Image().setUrl("https://actions.o2o.kr/devsvr1/image/drama9.jpg")
							.setAccessibilityText("나의 아저씨"))
                    .setFormattedText(" ● 줄거리 : " + synopsis + " ● 배우 : " + actors);
			data.put("netflixId","81267691");
		} else if (selectedItem.equals("11") || drama.equals("멜로가체질")) {
			simpleResponse2
					.setTextToSpeech("네, 멜로가 체질에 대해 알려드릴게요");
			String synopsis = "스타 드라마 작가로 우뚝 설 그날만을 꿈꾸는 여자. 젊은 나이에 다큐멘터리 감독으로 성공한 여자. 일하느라 혼자 아들 키우느라 정신없이 살아가는 여자. 각기 다른 상황에서 일과 연애를 모두 잡으려 애쓰는 서른 살 그녀들의 이야기.";
			String actors = "천우희, 전여빈, 한지은";
			basicCard
					.setTitle("멜로가 체질")
					.setImage(new Image().setUrl("https://actions.o2o.kr/devsvr1/image/drama11.jpg")
							.setAccessibilityText("멜로가 체질"))
                    .setFormattedText(" ● 줄거리 : " + synopsis + " ● 배우 : " + actors);
			data.put("netflixId","81211284");
		} else if (selectedItem.equals("12") || drama.equals("알함브라궁전의추억")) {
			simpleResponse2
					.setTextToSpeech("네, 알함브라 궁전의 추억에 대해 알려드릴게요");
			String synopsis = "투자회사 대표인 남자주인공이 비즈니스로 스페인 그라나다에 갔다가 전직 기타리스트였던 여주인공이 운영하는 싸구려 호스텔에 묵으며 두 사람이 기묘한 사건에 휘말리며 펼쳐지는 이야기";
			String actors = "현빈, 박신혜, 박훈";
			basicCard
					.setTitle("알함브라 궁전의 추억")
					.setImage(new Image().setUrl("https://actions.o2o.kr/devsvr1/image/drama12.jpg")
							.setAccessibilityText("알함브라 궁전의 추억"))
                    .setFormattedText(" ● 줄거리 : " + synopsis + " ● 배우 : " + actors);
			data.put("netflixId","81004280");
		} else if (selectedItem.equals("13") || drama.equals("어비스")) {
			simpleResponse2
					.setTextToSpeech("네, 어비스에 대해 알려드릴게요");
			String synopsis = "\"영혼 소생 구슬\" 어비스를 통해 생전과 180도 다른 '반전 비주얼'로 부활한 두 남녀가 자신을 죽인 살인자를 쫓는 반전 비주얼 판타지";
			String actors = "박보영, 안효섭, 이성재";
			basicCard
					.setTitle("어비스")
					.setImage(new Image().setUrl("https://actions.o2o.kr/devsvr1/image/drama13.jpg")
							.setAccessibilityText("어비스"))
                    .setFormattedText(" ● 줄거리 : " + synopsis + " ● 배우 : " + actors);
			data.put("netflixId","81087762");
		} else if (selectedItem.equals("15") || drama.equals("인간수업")) {
			simpleResponse2
					.setTextToSpeech("네, 인간수업에 대해 알려드릴게요");
			String synopsis = "돈을 벌기 위해 죄책감없이 범죄의 길을 선택한 고등학생들이 그로 인해 돌이킬 수 없이 혹독한 대가를 치르는 과정을 그린 넷플릭스 오리지널 시리즈";
			String actors = "김동희, 정다빈, 박주현";
			basicCard
					.setTitle("인간수업")
					.setImage(new Image().setUrl("https://actions.o2o.kr/devsvr1/image/drama15.jpg")
							.setAccessibilityText("인간수업"))
                    .setFormattedText(" ● 줄거리 : " + synopsis + " ● 배우 : " + actors);
			data.put("netflixId","80990668");
		} else if (selectedItem.equals("16") || drama.equals("보이스")) {
			simpleResponse2
					.setTextToSpeech("네, 보이스에 대해 알려드릴게요");
			String synopsis = "소리에서 단서를 찾는 '보이스 프로파일러'의 세계. 112 신고센터장 권주와 열혈 형사 진혁이 생사의 갈림길에 선 사람들을 구하기 위해 작은 소리 하나도 놏히지 않는다. 골든타임의 경계를 넘나드는 그들의 활약. 오늘도 출동이다.";
			String actors = "이하나, 장혁, 이진욱";
			basicCard
					.setTitle("보이스")
					.setImage(new Image().setUrl("https://actions.o2o.kr/devsvr1/image/drama16.jpg")
							.setAccessibilityText("보이스"))
                    .setFormattedText(" ● 줄거리 : " + synopsis + " ● 배우 : " + actors);
			data.put("netflixId","80187302");
		} else if (selectedItem.equals("14") || drama.equals("시그널")) {
			simpleResponse2
					.setTextToSpeech("네, 시그널에 대해 알려드릴게요");
			String synopsis = "\"우리의 시간은 이어져있다.\" 과거로부터 걸려온 간절한 신호(무전)로 연결된 현재와 과거의 형사들이 오래된 미제 사건들을 다시 파헤친다!";
			String actors = "이제훈, 김혜수, 조진웅";
			basicCard
					.setTitle("시그널")
					.setImage(new Image().setUrl("https://actions.o2o.kr/devsvr1/image/drama14.jpg")
							.setAccessibilityText("시그널"))
                    .setFormattedText(" ● 줄거리 : " + synopsis + " ● 배우 : " + actors);
			data.put("netflixId","80987077");
		} else if (selectedItem.equals("17") || drama.equals("킹덤")) {
			simpleResponse2
					.setTextToSpeech("네, 킹덤에 대해 알려드릴게요");
			String synopsis = "죽은 자들이 살아나 생지옥이 된 위기의 조선, 왕권을 탐하는 조씨 일가의 탐욕과 누구도 믿을 수 없게 되어버린 왕세자 창의 피의 사투를 그린 미스터리 스릴러";
			String actors = "주지훈, 류승룡, 배두나";
			basicCard
					.setTitle("킹덤")
					.setImage(new Image().setUrl("https://actions.o2o.kr/devsvr1/image/drama17.jpg")
							.setAccessibilityText("킹덤"))
                    .setFormattedText(" ● 줄거리 : " + synopsis + " ● 배우 : " + actors);
			data.put("netflixId","80180171");
		} else if (selectedItem.equals("18") || drama.equals("미스터션샤인")) {
			simpleResponse2
					.setTextToSpeech("네, 미스터 션샤인에 대해 알려드릴게요");
			String synopsis = "신미양요(1871년) 때 군함에 승선해 미국에 떨어진 한 소년이 미국 군인 신분으로 자신을 버린 조국인 조선으로 돌아와 주둔하며 벌어지는 일을 그린 드라마";
			String actors = "이병헌, 김태리, 유연석";
			basicCard
					.setTitle("미스터 션샤인")
					.setImage(new Image().setUrl("https://actions.o2o.kr/devsvr1/image/drama18.jpg")
							.setAccessibilityText("미스터 션샤인"))
                    .setFormattedText(" ● 줄거리 : " + synopsis + " ● 배우 : " + actors);
			data.put("netflixId","80991107");
		} else if (selectedItem.equals("19") || drama.equals("더킹")) {
			simpleResponse2
					.setTextToSpeech("네, 더 킹 - 영원의 군주에 대해 알려드릴게요");
			String synopsis = "악마에 맞서 차원의 문(門)을 닫으려는 이과(理科)형 대한제국 황제와 누군가의 삶·사람·사랑을 지키려는 문과(文科)형 대한민국 형사의 공조를 통해 차원이 다른 로맨스를 그린 드라마";
			String actors = "이민호, 김고은, 우도환";
			basicCard
					.setTitle("더 킹 - 영원의 군주")
					.setImage(new Image().setUrl("https://actions.o2o.kr/devsvr1/image/drama19.jpg")
							.setAccessibilityText("더 킹 - 영원의 군주"))
                    .setFormattedText(" ● 줄거리 : " + synopsis + " ● 배우 : " + actors);
			data.put("netflixId","81260283");
		} else if (selectedItem.equals("10") || drama.equals("도깨비")) {
			simpleResponse2
					.setTextToSpeech("네, 도깨비에 대해 알려드릴게요");
			String synopsis = "신부를 찾아야 죽을 수 있는 남자. 불멸의 고통에 힘겹던 어느 날, 신부라고 주장하는 여학생이 나타났다. 대책 없이 그를 소환하고 대책 없이 삶에 파고드는 소녀. 정녕 신부를 만난 것이냐. 그럼 이제 소멸할 수 있는 것이냐.";
			String actors = "공유, 김고은, 이동욱";
			basicCard
					.setTitle("도깨비")
					.setImage(new Image().setUrl("https://actions.o2o.kr/devsvr1/image/drama10.jpg")
							.setAccessibilityText("도깨비"))
                    .setFormattedText(" ● 줄거리 : " + synopsis + " ● 배우 : " + actors);
			data.put("netflixId","81012510");
		} else if (selectedItem.equals("20") || drama.equals("디어마이프렌즈")) {
			simpleResponse2
					.setTextToSpeech("네, 디어 마이 프렌즈에 대해 알려드릴게요");
			String synopsis = "자식들 뒷바리지하랴, 가족들 건사하랴, 어느새 축 지나가 버린 세월. 그렇게 노년에 접어들었지만 인생, 아직 저물지 않았다. '시니어벤저스'와 노희경 표 반짝이는 명대사의 만남, 꼰대들의 유쾌한 인생 찬가가 펼쳐진다.";
			String actors = "고현정, 김혜자, 고두심";
			basicCard
					.setTitle("디어 마이 프렌즈")
					.setImage(new Image().setUrl("https://actions.o2o.kr/devsvr1/image/drama20.jpg")
							.setAccessibilityText("디어 마이 프렌즈"))
                    .setFormattedText(" ● 줄거리 : " + synopsis + " ● 배우 : " + actors);
			data.put("netflixId","81267633");
		}

		responseBuilder.add(simpleResponse2);
		responseBuilder.add(basicCard);
		responseBuilder.add("시청하시겠어요??");

		return responseBuilder.build();
	}

	/**
	 * 드라마를 시청하면 넷플릭스에 연결하는 인텐트
	 *
	 * @param request
	 * @return
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	@ForIntent("Watch")
	public ActionResponse watch(ActionRequest request) throws ExecutionException, InterruptedException {
		ResponseBuilder responseBuilder = getResponseBuilder(request);
		Map<String, Object> data = responseBuilder.getConversationData();

		String netflixId = CommonUtil.makeSafeString(data.get("netflixId"));

		SimpleResponse simpleResponse = new SimpleResponse();
		LinkOutSuggestion linkOutSuggestion = new LinkOutSuggestion();
		BasicCard basicCard = new BasicCard();

		simpleResponse
				.setTextToSpeech("넷플릭스를 연결할게요");
		basicCard
				.setImage(
						new Image()
								.setUrl("https://actions.o2o.kr/devsvr1/image/netflix.png")
				);

		linkOutSuggestion
				.setDestinationName("넷플릭스 연결")
				.setUrl("https://www.netflix.com/title/" + netflixId);

		responseBuilder.add(simpleResponse);
		responseBuilder.add(basicCard);
		responseBuilder.add(linkOutSuggestion).endConversation();

		return responseBuilder.build();
	}

	/**
	 * 드라마를 시청하지 않는다고 하면 처음으로 돌아가는 인텐트
	 *
	 * @param request
	 * @return
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	@ForIntent("Not Watch")
	public ActionResponse notWatch(ActionRequest request) throws ExecutionException, InterruptedException {
		ResponseBuilder responseBuilder = getResponseBuilder(request);
		Map<String, Object> data = responseBuilder.getConversationData();

		data.clear();

		SimpleResponse simpleResponse = new SimpleResponse();
		BasicCard basicCard = new BasicCard();
		List<String> suggestions = new ArrayList<String>();

		simpleResponse
				.setTextToSpeech("다른 드라마를 추천해드릴게요. 장르, 배우, 작가 중 하나를 선택해주세요.")
				.setDisplayText("다른 드라마를 추천해드릴게요.")
		;

		basicCard
				.setImage(
						new Image()
								.setUrl("https://actions.o2o.kr/devsvr1/image/home.png")
				);

		suggestions.add("장르");
		suggestions.add("배우");
		suggestions.add("작가");

		responseBuilder.add(simpleResponse);
		responseBuilder.add(basicCard);
		responseBuilder.addSuggestions(suggestions.toArray(new String[suggestions.size()]));

		return responseBuilder.build();
	}
}
