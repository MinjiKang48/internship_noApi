package com.o2o.action.server.app;

import com.google.actions.api.ActionRequest;
import com.google.actions.api.ActionResponse;
import com.google.actions.api.DialogflowApp;
import com.google.actions.api.ForIntent;
import com.google.actions.api.response.ResponseBuilder;
import com.google.api.services.actions_fulfillment.v2.model.BasicCard;
import com.google.api.services.actions_fulfillment.v2.model.Image;
import com.google.api.services.actions_fulfillment.v2.model.SimpleResponse;
import com.o2o.action.server.util.CommonUtil;

import java.util.ArrayList;
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

	@ForIntent("chosenActor")
	public ActionResponse chosenActor(ActionRequest request) throws ExecutionException, InterruptedException {
		ResponseBuilder responseBuilder = getResponseBuilder(request);
		Map<String, Object> data = responseBuilder.getConversationData();

		data.clear();

		responseBuilder
				.add("어떤 배우의 작품을 보고싶으신가요?")
				.addSuggestions(new String[]{"최우식", "정유미", "배수지", "박서준"});

		return responseBuilder.build();
	}

//	@ForIntent("chosenGenre")
//	public ActionResponse chosenGenre(ActionRequest request) throws ExecutionException, InterruptedException {
//		ResponseBuilder rb = getResponseBuilder(request);
//		Map<String, Object> data = rb.getConversationData();
//
//		data.clear();
//
//		rb
//				.add("어떤 장르의 드라마를 보고싶으신가요?")
//				.addSuggestions(new String[] {"범죄", "판타지", "로맨스", "10대"});
//
//		return rb.build();
//	}
//
//	@ForIntent("chosenYear")
//	public ActionResponse chosenYear(ActionRequest request) throws ExecutionException, InterruptedException {
//		ResponseBuilder rb = getResponseBuilder(request);
//		Map<String, Object> data = rb.getConversationData();
//
//		data.clear();
//
//		rb
//				.add("언제 방영된 드라마를 보고싶으신가요?")
//				.addSuggestions(new String[] {"2017년", "2018년", "2019년", "2020년"});
//
//		return rb.build();
//	}

	@ForIntent("chosenActorDramaList")
	public ActionResponse chosenActorDrama(ActionRequest request) throws ExecutionException, InterruptedException {
		ResponseBuilder responseBuilder = getResponseBuilder(request);
		Map<String, Object> data = responseBuilder.getConversationData();

		data.clear();

//		SelectionList selectionList = new SelectionList();
		SimpleResponse simpleResponse = new SimpleResponse();

		String actor = CommonUtil.makeSafeString(request.getParameter("actor"));

		//해당 배우의 작품 리스트
//		selectionList
//				.setTitle(actor + "의 드라마")
//				.setItems(
//						Arrays.asList(
//								new ListSelectListItem()
//										.setTitle("인간수업")
//										.setDescription("인간수업은 어쩌구 저쩌구")
//										.setImage(
//												new Image()
//														.setUrl("https://actions.o2o.kr/devsvr1/image/IweRIPTD-uu14KRMiYuyMihogoUY.jpg")
//														.setAccessibilityText("인간수업 포스터")
//										)
//										.setOptionInfo(
//												new OptionInfo()
//														.setSynonyms(
//																Arrays.asList("1", "2", "3")
//														)
//														.setKey("SELECTION_KEY_ONE")
//										)));

		simpleResponse.setTextToSpeech("어떤 드라마를 선택하시겠어요?") //소리
				.setDisplayText("원하는 드라마를 선택해주세요."); //글 표시

//		rb.add(selectionList);
		responseBuilder.add(simpleResponse);

		return responseBuilder.build();
	}

//	@ForIntent("chosenGenreDrama")
//	public ActionResponse chosenGenreDrama(ActionRequest request) throws ExecutionException, InterruptedException {
//		ResponseBuilder rb = getResponseBuilder(request);
//		Map<String, Object> data = rb.getConversationData();
//
//		data.clear();
//
//		SelectionList selectionList = new SelectionList();
//		SimpleResponse simpleResponse = new SimpleResponse();
//
//		String genre = CommonUtil.makeSafeString(request.getParameter("genre"));
//
//			//장르에 관련된 드라마 리스트
//		selectionList
//					.setTitle(genre + " 드라마")
//					.setItems(
//							Arrays.asList(
//									new ListSelectListItem()
//									.setTitle("인간수업")
//									.setDescription("인간수업은 어쩌구 저쩌구")
//									.setImage(
//											new Image()
//													.setUrl("https://actions.o2o.kr/devsvr1/image/IweRIPTD-uu14KRMiYuyMihogoUY.jpg")
//													.setAccessibilityText("인간수업 포스터")
//									)
//									.setOptionInfo(
//											new OptionInfo()
//												.setSynonyms(
//														Arrays.asList("1", "2", "3")
//												)
//												.setKey("SELECTION_KEY_ONE")
//							)));
//
//			simpleResponse.setTextToSpeech("어떤 드라마를 선택하시겠어요?") //소리
//					.setDisplayText("원하는 드라마를 선택해주세요.") //글 표시
//			;
//
//
//		rb.add(selectionList);
//		rb.add(simpleResponse);
//
//		return rb.build();
//	}
//
//	@ForIntent("chosenYearDrama")
//	public ActionResponse chosenYearDrama(ActionRequest request) throws ExecutionException, InterruptedException {
//		ResponseBuilder rb = getResponseBuilder(request);
//		Map<String, Object> data = rb.getConversationData();
//
//		data.clear();
//
//		SelectionList selectionList = new SelectionList();
//		SimpleResponse simpleResponse = new SimpleResponse();
//
//		String year = CommonUtil.makeSafeString(request.getParameter("date-time"));
//
//		//장르에 관련된 드라마 리스트
//		selectionList
//				.setTitle(year + "에 방영된 드라마")
//				.setItems(
//						Arrays.asList(
//								new ListSelectListItem()
//										.setTitle("인간수업")
//										.setDescription("인간수업은 어쩌구 저쩌구")
//										.setImage(
//												new Image()
//														.setUrl("https://actions.o2o.kr/devsvr1/image/IweRIPTD-uu14KRMiYuyMihogoUY.jpg")
//														.setAccessibilityText("인간수업 포스터")
//										)
//										.setOptionInfo(
//												new OptionInfo()
//														.setSynonyms(
//																Arrays.asList("1", "2", "3")
//														)
//														.setKey("SELECTION_KEY_ONE")
//										)));
//
//		simpleResponse.setTextToSpeech("어떤 드라마를 선택하시겠어요?") //소리
//				.setDisplayText("원하는 드라마를 선택해주세요.") //글 표시
//		;
//
//
//		rb.add(selectionList);
//		rb.add(simpleResponse);
//
//		return rb.build();
//	}

	@ForIntent("detailedDrama") //현재의 문제 : simpleResponse 말고 다른 response의 형태가 들어오면 실행 오류가 발생한다.
	public ActionResponse detailedDrama(ActionRequest request) throws ExecutionException, InterruptedException {
		ResponseBuilder responseBuilder = getResponseBuilder(request);
		Map<String, Object> data = responseBuilder.getConversationData();

		data.clear();

		SimpleResponse simpleResponse = new SimpleResponse();
//        BasicCard basicCard = new BasicCard();

		String drama = CommonUtil.makeSafeString(request.getParameter("drama"));

//        basicCard
//                .setTitle(drama)
//                .setImage(new Image().setUrl("https://actions.o2o.kr/devsvr1/image/인간수업포스터.jpg")
//                        .setAccessibilityText("인간수업 포스터"));

		simpleResponse
				.setTextToSpeech("시청하시겠어요?")
				.setDisplayText("시청하시겠어요?");

//		responseBuilder.add(basicCard);
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

