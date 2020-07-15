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

		simpleResponse.setTextToSpeech("반가워요, 취향에 맞는 드라마를 추천해드릴게요.") //소리
				.setDisplayText("안녕하세요, 취향에 맞는 드라마를 추천해드릴게요.") //글 표시
		;

		basicCard
				.setTitle("재밌는 드라마 추천")
				.setImage(new Image().setUrl("https://actions.o2o.kr/devsvr1/image/home.gif")
						.setAccessibilityText("home"));

		SimpleResponse simpleResponse2 = new SimpleResponse();
		simpleResponse2.setTextToSpeech("장르, 배우 혹은 작가를 선택해주세요.");

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
	 * 배우,장르 혹은 연도 중 하나를 선택하면 나오는 또 다른 선택
	 * @param request
	 * @return
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	@ForIntent("choice")
	public ActionResponse chosenActor(ActionRequest request) throws ExecutionException, InterruptedException {
		ResponseBuilder responseBuilder = getResponseBuilder(request);
		Map<String, Object> data = responseBuilder.getConversationData();

		data.clear();

		List<String> suggestions = new ArrayList<String>();
		SimpleResponse simpleResponse = new SimpleResponse();

		String choice = CommonUtil.makeSafeString(request.getParameter("choice"));

		if(choice.equals("배우")) { //배우 선택
			simpleResponse
					.setTextToSpeech("어떤 배우의 드라마를 보고싶으신가요?");
			suggestions.add("최우식");
			suggestions.add("정유미");
			suggestions.add("박서준");
			suggestions.add("배수지");
		} else if(choice.equals("장르")) { //장르 선택
			simpleResponse
					.setTextToSpeech("어떤 장르의 드라마를 보고싶으신가요?");
			suggestions.add("범죄");
			suggestions.add("판타지");
			suggestions.add("로맨스");
			suggestions.add("10대");
		} else if(choice.equals("작가")) { //작가 선택
			simpleResponse
					.setTextToSpeech("어떤 작가의 드라마를 보고싶으신가요?");
			suggestions.add("김은숙");
			suggestions.add("김은희");
			suggestions.add("박지은");
			suggestions.add("노희경");
		}

		responseBuilder.add(simpleResponse);
		responseBuilder.addSuggestions(suggestions.toArray(new String[suggestions.size()]));

		return responseBuilder.build();
	}

	/**
	 * 선택한 장르, 배우 혹은 연도에 대한 드라마 목록 제시
	 * @param request
	 * @return
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	@ForIntent("chosenDramaList")
	public ActionResponse chosenActorDrama(ActionRequest request) throws ExecutionException, InterruptedException {
		ResponseBuilder responseBuilder = getResponseBuilder(request);
		Map<String, Object> data = responseBuilder.getConversationData();

		data.clear();

		SimpleResponse simpleResponse = new SimpleResponse();
		SelectionList selectionList = new SelectionList();

		String actor = CommonUtil.makeSafeString(request.getParameter("actor")); //actor entity에는 : (현재) 최우식, 정유미, 박서준, 배수지
		String genre = CommonUtil.makeSafeString(request.getParameter("genre")); //genre entity에는  : (현재) 범죄, 판타지, 로맨스, 코미디, 10대
		String author = CommonUtil.makeSafeString(request.getParameter("author")); //year entity에는 : (현재) 김은숙, 김은희, 노희경, 박지은

		if(genre.equals("") && author.equals("")) { //배우를 선택해서 해당 배우의 드라마 목록을 보여줌
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
											,
									new ListSelectListItem()
											.setTitle("Google Home")
											.setDescription(
													"Google Home is a voice-activated speaker powered by the Google Assistant.")
											.setImage(
													new Image()
															.setUrl(
																	"https://actions.o2o.kr/devsvr1/image/" + actor + "드라마2.jpg")
															.setAccessibilityText(actor + "드라마2"))
											,
									new ListSelectListItem()
											.setTitle("Google Pixel")
											.setDescription("Pixel. Phone by Google.")
											.setImage(
													new Image()
															.setUrl(
																	"https://actions.o2o.kr/devsvr1/image/" + actor + "드라마3.jpg")
															.setAccessibilityText(actor + "드라마3"))
											));
		} else if(actor.equals("") && author.equals("")) { //장르를 선택해서 해당 장르의 드라마를 보여줌
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
											,
									new ListSelectListItem()
											.setTitle("Google Home")
											.setDescription(
													"Google Home is a voice-activated speaker powered by the Google Assistant.")
											.setImage(
													new Image()
															.setUrl(
																	"https://actions.o2o.kr/devsvr1/image/" + genre + "드라마2.jpg")
															.setAccessibilityText(genre + "드라마2"))
											,
									new ListSelectListItem()
											.setTitle("Google Pixel")
											.setDescription("Pixel. Phone by Google.")
											.setImage(
													new Image()
															.setUrl(
																	"https://actions.o2o.kr/devsvr1/image/" + genre + "드라마3.jpg")
															.setAccessibilityText(genre + "드라마3"))
											));
		} else if(actor.equals("") && genre.equals("")) { //연도를 선택해서 해당 연도 방영 드라마 목록 보여줌
			selectionList
					.setTitle(author + "작가의 드라마")
					.setItems(
							Arrays.asList(
									new ListSelectListItem()
											.setTitle("인간수업")
											.setDescription("인간수업은 어쩌구 저쩌구")
											.setImage(
													new Image()
															.setUrl(
																	"https://actions.o2o.kr/devsvr1/image/" + author + "드라마1.jpg")
															.setAccessibilityText(author + "드라마1"))
											,
									new ListSelectListItem()
											.setTitle("Google Home")
											.setDescription(
													"Google Home is a voice-activated speaker powered by the Google Assistant.")
											.setImage(
													new Image()
															.setUrl(
																	"https://actions.o2o.kr/devsvr1/image/" + author + "드라마2.jpg")
															.setAccessibilityText(author + "드라마2"))
											,
									new ListSelectListItem()
											.setTitle("Google Pixel")
											.setDescription("Pixel. Phone by Google.")
											.setImage(
													new Image()
															.setUrl(
																	"https://actions.o2o.kr/devsvr1/image/" + author + "드라마3.jpg")
															.setAccessibilityText(author + "드라마3"))
											));
		}

		simpleResponse.setTextToSpeech("어떤 드라마를 선택하시겠어요?")
				.setDisplayText("원하는 드라마를 선택해주세요.");

		responseBuilder.add(selectionList);
		responseBuilder.add(simpleResponse);

		return responseBuilder.build();
	}

	/**
	 * 드라마 목록 중 선택한 드라마에 대한 설명
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

		SimpleResponse simpleResponse = new SimpleResponse();
		SimpleResponse simpleResponse2 = new SimpleResponse();
        BasicCard basicCard = new BasicCard();

        //여러가지 드라마를 drama 엔티티로 받음
		String drama = CommonUtil.makeSafeString(request.getParameter("drama"));

		simpleResponse2
				.setTextToSpeech("네, " + drama + "에 대해 알려드릴게요");

		//해당 드라마의 포스터와 간단 줄거리 등을 보여줌
		basicCard
				.setTitle(drama)
				.setImage(new Image().setUrl("https://actions.o2o.kr/devsvr1/image/최우식드라마1.jpg")
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

	/**
	 * 드라마를 시청하면 넷플릭스에 연결
	 * @param request
	 * @return
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	@ForIntent("watch")
	public ActionResponse watch(ActionRequest request) throws ExecutionException, InterruptedException {
		ResponseBuilder responseBuilder = getResponseBuilder(request);
		Map<String, Object> data = responseBuilder.getConversationData();

		data.clear();

		SimpleResponse simpleResponse = new SimpleResponse();
		LinkOutSuggestion linkOutSuggestion = new LinkOutSuggestion();

		simpleResponse
				.setTextToSpeech("넷플릭스를 연결할게요");

		linkOutSuggestion
				.setDestinationName("넷플릭스 연결")
				.setUrl("https://play.google.com/store/apps/details?id=com.netflix.mediaclient");

		responseBuilder.add(simpleResponse);
		responseBuilder.add(linkOutSuggestion);

		return responseBuilder.build();
	}

	/**
	 * 드라마를 시청하지 않는다고 하면 다른 드라마를 선택할 수 있도록 한다.
	 * @param request
	 * @return
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	@ForIntent("No Watch") //실행되지 않음 (??? 왜)
	public ActionResponse noWatch(ActionRequest request) throws ExecutionException, InterruptedException {
		ResponseBuilder responseBuilder = getResponseBuilder(request);
		Map<String, Object> data = responseBuilder.getConversationData();

		data.clear();

		SimpleResponse simpleResponse = new SimpleResponse();

		simpleResponse
				.setDisplayText("다른 드라마를 선택해주세요.")
				.setTextToSpeech("다른 드라마를 선택해주세요");

		chosenActorDrama(request);

		return responseBuilder.build();
	}
}

