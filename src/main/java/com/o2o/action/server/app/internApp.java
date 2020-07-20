package com.o2o.action.server.app;

import com.google.actions.api.ActionRequest;
import com.google.actions.api.ActionResponse;
import com.google.actions.api.DialogflowApp;
import com.google.actions.api.ForIntent;
import com.google.actions.api.response.ResponseBuilder;
import com.google.actions.api.response.helperintent.SelectionList;
import com.google.api.services.actions_fulfillment.v2.model.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.o2o.action.server.util.CommonUtil;

import java.util.ArrayList;
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
		simpleResponse2.setTextToSpeech("국가 혹은 장르를 선택해주세요.");

		suggestions.add("국가");
		suggestions.add("장르");

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
	public ActionResponse choice(ActionRequest request) throws ExecutionException, InterruptedException {
		ResponseBuilder responseBuilder = getResponseBuilder(request);
		Map<String, Object> data = responseBuilder.getConversationData();

		data.clear();

		List<String> suggestions = new ArrayList<String>();
		SimpleResponse simpleResponse = new SimpleResponse();

		String choice = CommonUtil.makeSafeString(request.getParameter("choice"));

		if(choice.equals("나라") || choice.equals("국가")) { //나라 선택
			simpleResponse
					.setTextToSpeech("어떤 나라의 드라마를 보고싶으신가요?");
			suggestions.add("한국");
			suggestions.add("미국");
			suggestions.add("영국");
			suggestions.add("스페인");
		} else if(choice.equals("장르")) { //장르 선택
			simpleResponse
					.setTextToSpeech("어떤 장르의 드라마를 보고싶으신가요?");
			suggestions.add("범죄");
			suggestions.add("판타지");
			suggestions.add("로맨틱코미디");
			suggestions.add("10대");
		}

		responseBuilder.add(simpleResponse);
		responseBuilder.addSuggestions(suggestions.toArray(new String[suggestions.size()]));

		return responseBuilder.build();
	}

	public static void main(String[] args) {
		ApiController apcon = new ApiController();
		//System.out.println(apcon.getGenre());

		JsonParser jsonParser = new JsonParser();
		DramaList dramaList = new DramaList();
//		JsonObject jsonobj = (JsonObject) jsonParser.parse(apcon.getCountriesList(348));
		// 받아온 정보의 에러코드 확인
		JsonObject jsonobj = (JsonObject) jsonParser.parse(apcon.getCountriesList(348));
		JsonArray results = jsonobj.get("results").getAsJsonArray();
		String cnt = jsonobj.get("total").toString();
		int count = Integer.parseInt(cnt);
		System.out.println(cnt);
		for(int i = 0; i < 100; i++){
			Drama drama = new Drama();
			JsonObject element = results.get(i).getAsJsonObject();
			drama.title = element.get("title").toString();
			drama.imgUrl = element.get("img").toString();
			drama.id = element.get("id").toString();
			drama.nid = element.get("nfid").toString();
			drama.synopsis = element.get("synopsis").toString();
			drama.titleDate = element.get("titledate").toString();
			drama.avgrating = element.get("avgrating").toString();
			dramaList.items.add(drama);
		}

//		for(int i = 0; i < dramaList.items.size(); i++){
//			System.out.println(dramaList.items.get(i).title);
//		}

		ArrayList<ListSelectListItem> list = new ArrayList<ListSelectListItem>();
		for(int i = 0; i < dramaList.items.size(); i++) {
			list.add(
					new ListSelectListItem()
							.setTitle(dramaList.items.get(i).title)
							.setDescription(dramaList.items.get(i).synopsis)
							.setImage(
									new Image()
											.setUrl(
													dramaList.items.get(i).imgUrl)
											.setAccessibilityText(dramaList.items.get(i).title))
							.setOptionInfo(
									new OptionInfo()
											.setKey(dramaList.items.get(i).id)));
		}

		for(int i = 0; i < list.size(); i++)
			System.out.println(list.get(i).getTitle());
	}

	DramaList dramaList = new DramaList();

	/**
	 * 선택한 장르, 국가에 대한 드라마 목록 제시
	 * @param request
	 * @return
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	@ForIntent("chosenDramaList")
	public ActionResponse chosenDramaList(ActionRequest request) throws ExecutionException, InterruptedException {
		ResponseBuilder responseBuilder = getResponseBuilder(request);
		Map<String, Object> data = responseBuilder.getConversationData();

		data.clear();

		SimpleResponse simpleResponse = new SimpleResponse();
		SelectionList selectionList = new SelectionList();

		final String countries = CommonUtil.makeSafeString(request.getParameter("countries")); //countries : (현재) 미국, 영국, 스페인, 한국
		final String genre = CommonUtil.makeSafeString(request.getParameter("genre")); //genre : (현재) 범죄, 판타지, 로맨틱코미디, 10대

		JsonArray result = new JsonArray();
		if(genre.equals("")) {//countries 선택
			switch(countries) {
				case "한국":
					result = processCountries(348);
					break;
				case "미국":
					result = processCountries(78);
					break;
				case "영국":
					result = processCountries(46);
					break;
				case "스페인":
					result = processCountries(270);
					break;
			}

		} else if(countries.equals("")) {
			switch(genre) {
				case "로맨틱코미디":
					processGenre(5475);
					break;
				case "10대":
					processGenre(9299);
					break;
				case "판타지":
					processGenre(1568);
					break;
				case "범죄":
					processGenre(68889);
					break;
			}
		}

		ArrayList<ListSelectListItem> list = new ArrayList<>();

		for(int i = 0; i < result.size(); i++) {
			ListSelectListItem listItem = new ListSelectListItem();
//			list.add(
//					new ListSelectListItem()
//							.setTitle(dramaList.items.get(i).title)
//							.setDescription(dramaList.items.get(i).synopsis)
//							.setImage(
//									new Image()
//											.setUrl(
//													dramaList.items.get(i).imgUrl)
//											.setAccessibilityText(dramaList.items.get(i).title))
//							.setOptionInfo(
//									new OptionInfo()
//											.setKey(dramaList.items.get(i).id)));
		}

		selectionList
				.setTitle("ee")
				.setItems(list);

		simpleResponse.setTextToSpeech("어떤 드라마를 선택하시겠어요?")
				.setDisplayText("원하는 드라마를 선택해주세요.");

		responseBuilder.add(selectionList);
		responseBuilder.add(simpleResponse);

		return responseBuilder.build();
	}

	private JsonArray processCountries(int id) {
		JsonParser jsonParser = new JsonParser(); ApiController apiController = new ApiController();
		JsonObject jsonobj = (JsonObject) jsonParser.parse(apiController.getCountriesList(id));
		JsonArray results = jsonobj.get("results").getAsJsonArray();
//		for(int i = 0; i < 8; i++){
//			Drama drama = new Drama();
//			JsonObject element = results.get(i).getAsJsonObject();
//			drama.title = element.get("title").toString();
//			drama.imgUrl = element.get("img").toString();
//			drama.id = element.get("id").toString();
//			drama.nid = element.get("nid").toString();
//			drama.synopsis = element.get("synopsis").toString();
//			drama.titleDate = element.get("titledate").toString();
//			drama.avgrating = element.get("avgrating").toString();
//			dramaList.items.add(drama);
//		}
		return results;
	}

	private void processGenre(int id) {
		JsonParser jsonParser = new JsonParser(); ApiController apiController = new ApiController();
		JsonObject jsonobj = (JsonObject) jsonParser.parse(apiController.getGenreList(id));
		JsonArray results = jsonobj.get("results").getAsJsonArray();
		for(int i = 0; i < 8; i++){
			Drama drama = new Drama();
			JsonObject element = results.get(i).getAsJsonObject();
			drama.title = element.get("title").toString();
			drama.imgUrl = element.get("img").toString();
			drama.id = element.get("id").toString();
			drama.nid = element.get("nid").toString();
			drama.synopsis = element.get("synopsis").toString();
			drama.titleDate = element.get("titledate").toString();
			drama.avgrating = element.get("avgrating").toString();
			dramaList.items.add(drama);
		}
	}

	/**
	 * 드라마 목록 중 선택한 드라마에 대한 설명
	 * @param request
	 * @return
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	String id;
	@ForIntent("Drama Description")
	public ActionResponse dramaDescription(ActionRequest request) throws ExecutionException, InterruptedException {
		ResponseBuilder responseBuilder = getResponseBuilder(request);
		Map<String, Object> data = responseBuilder.getConversationData();

		data.clear();

        String selectedItem = request.getSelectedOption();
        String drama = new String();

        int i;
        for(i = 0; i < dramaList.items.size(); i++){
        	if(selectedItem.equals(dramaList.items.get(i).id)) {
				drama = "You selected the" + i + "item";
				id = dramaList.items.get(i).nid;
			}
        	else
        		drama = CommonUtil.makeSafeString(request.getParameter("drama"));
		}
//        if (selectedItem.equals(dramaList.items.get(i).id)) {
//            drama = "You selected the first item";
//        } else if (selectedItem.equals("SELECTION_KEY_GOOGLE_HOME")) {
//            drama = "You selected the Google Home!";
//        } else if (selectedItem.equals("SELECTION_KEY_GOOGLE_PIXEL")) {
//            drama = "You selected the Google Pixel!";
//        } else {
//            drama = CommonUtil.makeSafeString(request.getParameter("drama"));
//        }

		SimpleResponse simpleResponse = new SimpleResponse();
		SimpleResponse simpleResponse2 = new SimpleResponse();
        BasicCard basicCard = new BasicCard();


		simpleResponse2
				.setTextToSpeech("네, " + dramaList.items.get(i).title + "에 대해 알려드릴게요");

		String synopsis = "줄거리 " + dramaList.items.get(i).synopsis + "\n";
		String titleDate = "방영일" + dramaList.items.get(i).titleDate + "\n";
		String avgrating = "평점" + dramaList.items.get(i).avgrating + "\n";

		//해당 드라마의 포스터와 간단 줄거리 등을 보여줌
		basicCard
				.setTitle(dramaList.items.get(i).title)
				.setImage(new Image().setUrl(dramaList.items.get(i).imgUrl)
						.setAccessibilityText(dramaList.items.get(i).title))
				.setFormattedText(synopsis + titleDate + avgrating);

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
				.setUrl("https://www.netflix.com/title/" + id);

		responseBuilder.add(simpleResponse);
		responseBuilder.add(linkOutSuggestion);

		return responseBuilder.build();
	}

	/**
	 * 드라마를 시청하지 않는다고 하면 아예 처음으로 돌아감
	 * @param request
	 * @return
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	@ForIntent("No Watch")
	public ActionResponse noWatch(ActionRequest request) throws ExecutionException, InterruptedException {
		ResponseBuilder responseBuilder = getResponseBuilder(request);
		Map<String, Object> data = responseBuilder.getConversationData();

		data.clear();

		SimpleResponse simpleResponse = new SimpleResponse();
		SimpleResponse simpleResponse2 = new SimpleResponse();
		List<String> suggestions = new ArrayList<String>();

		simpleResponse
				.setDisplayText("다른 드라마를 선택해주세요.")
				.setTextToSpeech("다른 드라마를 선택해주세요");

		simpleResponse2
				.setTextToSpeech("장르, 배우 혹은 작가를 선택해주세요.");

		suggestions.add("장르");
		suggestions.add("배우");
		suggestions.add("작가");

		responseBuilder.add(simpleResponse);
		responseBuilder.add(simpleResponse2);
		responseBuilder.addSuggestions(suggestions.toArray(new String[suggestions.size()]));

		return responseBuilder.build();
	}
}

