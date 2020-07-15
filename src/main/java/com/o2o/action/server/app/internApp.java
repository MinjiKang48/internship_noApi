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
	@ForIntent("Default Welcome Intent")
	public ActionResponse defaultWelcome(ActionRequest request) throws ExecutionException, InterruptedException {
		ResponseBuilder rb = getResponseBuilder(request);
		Map<String, Object> data = rb.getConversationData();

		data.clear();

		List<String> suggestions = new ArrayList<String>();
		SimpleResponse simpleResponse = new SimpleResponse(); //말풍선
		BasicCard basicCard = new BasicCard();

		simpleResponse.setTextToSpeech("반가워요, 취향에 맞는 드라마를 추천해드릴게요.") //소리
				.setDisplayText("안녕하세요, 취향에 맞는 드라마를 추천해드릴게요.") //글 표시
		;

		basicCard
				.setTitle("재밌는 드라마 추천")
				.setImage(new Image().setUrl("https://actions.o2o.kr/devsvr1/image/home.gif")
						.setAccessibilityText("home"));

		SimpleResponse simpleResponse2 = new SimpleResponse();
		simpleResponse2.setTextToSpeech("장르, 배우 혹은 연도를 선택해주세요.");

		suggestions.add("장르");
		suggestions.add("배우");
		suggestions.add("연도");

		rb.add(simpleResponse);
		rb.add(basicCard);
		rb.add(simpleResponse2);
		rb.addSuggestions(suggestions.toArray(new String[suggestions.size()]));

		return rb.build();
	}

	@ForIntent("choice")
	public ActionResponse chosenActor(ActionRequest request) throws ExecutionException, InterruptedException {
		ResponseBuilder responseBuilder = getResponseBuilder(request);
		Map<String, Object> data = responseBuilder.getConversationData();

		data.clear();

		List<String> suggestions = new ArrayList<String>();
		SimpleResponse simpleResponse = new SimpleResponse();

		String choice = CommonUtil.makeSafeString(request.getParameter("choice"));

		if(choice.equals("배우")) {
			simpleResponse
					.setTextToSpeech("어떤 배우의 드라마를 보고싶으신가요?");
			suggestions.add("최우식");
			suggestions.add("정유미");
			suggestions.add("박서준");
			suggestions.add("배수지");
		} else if(choice.equals("장르")) {
			simpleResponse
					.setTextToSpeech("어떤 장르의 드라마를 보고싶으신가요?");
			suggestions.add("범죄");
			suggestions.add("판타지");
			suggestions.add("로맨스");
			suggestions.add("10대");
		} else if(choice.equals("연도")) {
			simpleResponse
					.setTextToSpeech("언제 방영된 드라마를 보고싶으신가요?");
			suggestions.add("2017년");
			suggestions.add("2018년");
			suggestions.add("2019년");
			suggestions.add("2020년");
		}

		responseBuilder.add(simpleResponse);
		responseBuilder.addSuggestions(suggestions.toArray(new String[suggestions.size()]));

		return responseBuilder.build();
	}

	@ForIntent("chosenDramaList")
	public ActionResponse chosenActorDrama(ActionRequest request) throws ExecutionException, InterruptedException {
		ResponseBuilder responseBuilder = getResponseBuilder(request);
		Map<String, Object> data = responseBuilder.getConversationData();

		data.clear();

		SimpleResponse simpleResponse = new SimpleResponse();
		SelectionList selectionList = new SelectionList();

		String actor = CommonUtil.makeSafeString(request.getParameter("actor")); //custom entity를 만들어
		String genre = CommonUtil.makeSafeString(request.getParameter("genre"));
		String year = CommonUtil.makeSafeString(request.getParameter("year"));

		if(genre.equals("") && year.equals("")) {
			selectionList
					.setTitle(actor + "가 출연한 드라마")
					.setItems(
							Arrays.asList(
									new ListSelectListItem()
											.setTitle("인간수업")
											.setDescription("인간수업은 어쩌구 저쩌구")
											.setImage(
													new Image()
															.setUrl(
																	"https://actions.o2o.kr/devsvr1/image/" + actor + "드라마1.jpg")
															.setAccessibilityText(actor + "드라마1"))
											.setOptionInfo(
													new OptionInfo()
															.setSynonyms(
																	Arrays.asList("synonym 1", "synonym 2", "synonym 3"))
															.setKey("SELECTION_KEY_ONE")),
									new ListSelectListItem()
											.setTitle("Google Home")
											.setDescription(
													"Google Home is a voice-activated speaker powered by the Google Assistant.")
											.setImage(
													new Image()
															.setUrl(
																	"https://actions.o2o.kr/devsvr1/image/" + actor + "드라마2.jpg")
															.setAccessibilityText(actor + "드라마2"))
											.setOptionInfo(
													new OptionInfo()
															.setSynonyms(
																	Arrays.asList(
																			"Google Home Assistant",
																			"Assistant on the Google Home"))
															.setKey("SELECTION_KEY_GOOGLE_HOME")),
									new ListSelectListItem()
											.setTitle("Google Pixel")
											.setDescription("Pixel. Phone by Google.")
											.setImage(
													new Image()
															.setUrl(
																	"https://actions.o2o.kr/devsvr1/image/" + actor + "드라마3.jpg")
															.setAccessibilityText(actor + "드라마3"))
											.setOptionInfo(
													new OptionInfo()
															.setSynonyms(
																	Arrays.asList("Google Pixel XL", "Pixel", "Pixel XL"))
															.setKey("SELECTION_KEY_GOOGLE_PIXEL"))));
		} else if(actor.equals("") && year.equals("")) {
			selectionList
					.setTitle(genre + " 드라마")
					.setItems(
							Arrays.asList(
									new ListSelectListItem()
											.setTitle("인간수업")
											.setDescription("인간수업은 어쩌구 저쩌구")
											.setImage(
													new Image()
															.setUrl(
																	"https://actions.o2o.kr/devsvr1/image/" + genre + "드라마1.jpg")
															.setAccessibilityText(genre + "드라마1"))
											.setOptionInfo(
													new OptionInfo()
															.setSynonyms(
																	Arrays.asList("synonym 1", "synonym 2", "synonym 3"))
															.setKey("SELECTION_KEY_ONE")),
									new ListSelectListItem()
											.setTitle("Google Home")
											.setDescription(
													"Google Home is a voice-activated speaker powered by the Google Assistant.")
											.setImage(
													new Image()
															.setUrl(
																	"https://actions.o2o.kr/devsvr1/image/" + genre + "드라마2.jpg")
															.setAccessibilityText(genre + "드라마2"))
											.setOptionInfo(
													new OptionInfo()
															.setSynonyms(
																	Arrays.asList(
																			"Google Home Assistant",
																			"Assistant on the Google Home"))
															.setKey("SELECTION_KEY_GOOGLE_HOME")),
									new ListSelectListItem()
											.setTitle("Google Pixel")
											.setDescription("Pixel. Phone by Google.")
											.setImage(
													new Image()
															.setUrl(
																	"https://actions.o2o.kr/devsvr1/image/" + genre + "드라마3.jpg")
															.setAccessibilityText(genre + "드라마3"))
											.setOptionInfo(
													new OptionInfo()
															.setSynonyms(
																	Arrays.asList("Google Pixel XL", "Pixel", "Pixel XL"))
															.setKey("SELECTION_KEY_GOOGLE_PIXEL"))));
		} else if(actor.equals("") && genre.equals("")) {
			selectionList
					.setTitle(year + "에 방영된 드라마")
					.setItems(
							Arrays.asList(
									new ListSelectListItem()
											.setTitle("인간수업")
											.setDescription("인간수업은 어쩌구 저쩌구")
											.setImage(
													new Image()
															.setUrl(
																	"https://actions.o2o.kr/devsvr1/image/" + year + "드라마1.jpg")
															.setAccessibilityText(year + "드라마1"))
											.setOptionInfo(
													new OptionInfo()
															.setSynonyms(
																	Arrays.asList("synonym 1", "synonym 2", "synonym 3"))
															.setKey("SELECTION_KEY_ONE")),
									new ListSelectListItem()
											.setTitle("Google Home")
											.setDescription(
													"Google Home is a voice-activated speaker powered by the Google Assistant.")
											.setImage(
													new Image()
															.setUrl(
																	"https://actions.o2o.kr/devsvr1/image/" + year + "드라마2.jpg")
															.setAccessibilityText(year + "드라마2"))
											.setOptionInfo(
													new OptionInfo()
															.setSynonyms(
																	Arrays.asList(
																			"Google Home Assistant",
																			"Assistant on the Google Home"))
															.setKey("SELECTION_KEY_GOOGLE_HOME")),
									new ListSelectListItem()
											.setTitle("Google Pixel")
											.setDescription("Pixel. Phone by Google.")
											.setImage(
													new Image()
															.setUrl(
																	"https://actions.o2o.kr/devsvr1/image/" + year + "드라마3.jpg")
															.setAccessibilityText(year + "드라마3"))
											.setOptionInfo(
													new OptionInfo()
															.setSynonyms(
																	Arrays.asList("Google Pixel XL", "Pixel", "Pixel XL"))
															.setKey("SELECTION_KEY_GOOGLE_PIXEL"))));
		}

		simpleResponse.setTextToSpeech("어떤 드라마를 선택하시겠어요?") //소리
				.setDisplayText("원하는 드라마를 선택해주세요."); //글 표시

		responseBuilder.add(selectionList);
		responseBuilder.add(simpleResponse);

		return responseBuilder.build();
	}

	@ForIntent("Drama Description")
	public ActionResponse dramaDescription(ActionRequest request) throws ExecutionException, InterruptedException {
		ResponseBuilder responseBuilder = getResponseBuilder(request);
		Map<String, Object> data = responseBuilder.getConversationData();

		data.clear();

		SimpleResponse simpleResponse = new SimpleResponse();
		SimpleResponse simpleResponse2 = new SimpleResponse();
        BasicCard basicCard = new BasicCard();

		String drama = CommonUtil.makeSafeString(request.getParameter("drama"));

		simpleResponse2
				.setTextToSpeech("네, " + drama + "에 대해 알려드릴게요");

		basicCard
				.setTitle(drama)
				.setImage(new Image().setUrl("https://actions.o2o.kr/devsvr1/image/" + drama + "드라마1.jpg")
						.setAccessibilityText(drama))
				.setFormattedText(drama + "은 어쩌구 저쩌구");

		simpleResponse
				.setTextToSpeech("시청하시겠어요?")
				.setDisplayText("시청하시겠어요?");

		responseBuilder.add(simpleResponse2);
		responseBuilder.add(basicCard);
		responseBuilder.add(simpleResponse);

		return responseBuilder.build();
	}

	@ForIntent("watch")
	public ActionResponse watch(ActionRequest request) throws ExecutionException, InterruptedException {
		ResponseBuilder responseBuilder = getResponseBuilder(request);
		Map<String, Object> data = responseBuilder.getConversationData();

		data.clear();

		SimpleResponse simpleResponse = new SimpleResponse();
		simpleResponse
				.setTextToSpeech("넷플릭스를 연결할게요")
				.setDisplayText("넷플릭스 연결");

		responseBuilder.add(simpleResponse);
		return responseBuilder.build();
	}

	@ForIntent("No Watch")
	public ActionResponse noWatch(ActionRequest request) throws ExecutionException, InterruptedException {
		ResponseBuilder responseBuilder = getResponseBuilder(request);
		Map<String, Object> data = responseBuilder.getConversationData();

		data.clear();

		SimpleResponse simpleResponse = new SimpleResponse();

		simpleResponse
				.setDisplayText("다른 드라마를 선택해주세요.")
				.setTextToSpeech("다른 드라마를 선택해주세요");

		return responseBuilder.build();
	}
}

