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
		simpleResponse2.setTextToSpeech("장르, 배우 혹은 작가 중 하나를 선택해주세요.");

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

		if(choice.equals("배우")) { //나라 선택
			simpleResponse
					.setTextToSpeech("어떤 배우의 드라마를 보고싶으신가요?");
			suggestions.add("성동일");
			suggestions.add("서현진");
			suggestions.add("이지은(아이유)");
		} else if(choice.equals("장르")) { //장르 선택
			simpleResponse
					.setTextToSpeech("어떤 장르의 드라마를 보고싶으신가요?");
			suggestions.add("범죄");
			suggestions.add("판타지");
			suggestions.add("로맨틱코미디");
		} else if(choice.equals("작가")){
            simpleResponse
                    .setTextToSpeech("어떤 작가의 드라마를 보고싶으신가요?");
            suggestions.add("김은희");
            suggestions.add("김은숙");
            suggestions.add("");
        }

		responseBuilder.add(simpleResponse);
		responseBuilder.addSuggestions(suggestions.toArray(new String[suggestions.size()]));

		return responseBuilder.build();
	}

//	public static void main(String[] args) {
//		ApiController apcon = new ApiController();
//		//System.out.println(apcon.getGenre());
//
//		JsonParser jsonParser = new JsonParser();
//		DramaList dramaList = new DramaList();
////		JsonObject jsonobj = (JsonObject) jsonParser.parse(apcon.getCountriesList(348));
//		// 받아온 정보의 에러코드 확인
//		JsonObject jsonobj = (JsonObject) jsonParser.parse(apcon.getCountriesList("KR"));
//		JsonArray results = jsonobj.get("results").getAsJsonArray();
//		String cnt = jsonobj.get("total").toString();
//		int count = Integer.parseInt(cnt);
//		System.out.println(cnt);
//		for(int i = 0; i < 100; i++){
//			Drama drama = new Drama();
//			JsonObject element = results.get(i).getAsJsonObject();
//			drama.title = element.get("title").toString();
//			drama.imgUrl = element.get("img").toString();
//			drama.id = element.get("id").toString();
//			drama.synopsis = element.get("synopsis").toString();
//			drama.titleDate = element.get("titledate").toString();
//			drama.avgrating = element.get("avgrating").toString();
//			dramaList.items.add(drama);
//		}
//
////		for(int i = 0; i < dramaList.items.size(); i++){
////			System.out.println(dramaList.items.get(i).title);
////		}
//
//		ArrayList<ListSelectListItem> list = new ArrayList<ListSelectListItem>();
//		for(int i = 0; i < dramaList.items.size(); i++) {
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
//		}
//
//		for(int i = 0; i < list.size(); i++)
//			System.out.println(list.get(i).getTitle());
//	}

	DramaList dramaList = new DramaList();

	/**
	 * 선택한 장르 혹은 국가에 맞는 드라마 목록을 생성하는 인텐트
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

		final String actor = CommonUtil.makeSafeString(request.getParameter("actor")); //countries : (현재) 미국, 영국, 스페인, 한국
		final String genre = CommonUtil.makeSafeString(request.getParameter("genre")); //genre : (현재) 범죄, 판타지, 로맨틱코미디, 10대
        final String author = CommonUtil.makeSafeString(request.getParameter("author"));

		if(genre.equals("") && author.equals("")) { //actor 선택
			switch(actor) {
				case "성동일":
                    selectionList
                            .setTitle("성동일 배우의 드라마 목록")
                            .setItems(
                                    Arrays.asList(
                                            new ListSelectListItem()
                                            .setTitle("라이브")
                                            .setDescription("공권력의 상징 대한민국 경찰. 하지만 이들도 알고 보면 우리와 닮은 이웃일 뿐. 제복의 무게를 견디며 오늘도 정의를 위해 출동이다! 특별하고도 평범한 삶을 위하여.")
                                            .setImage(
                                                    new Image()
                                                            .setUrl("https://actions.o2o.kr/devsvr1/image/라이브.jpg")
                                                            .setAccessibilityText("라이브")
                                            )
                                            .setOptionInfo(
                                                    new OptionInfo()
                                                    .setKey("1")
                                            ),
                                            new ListSelectListItem()
                                                    .setTitle("응답하라 1988")
                                                    .setDescription("1988년, 쌍문동 골목길. 즐거움과 아픔을 함께 나누던 다섯 친구가 있다. 동네에서 함께 자라서 서로의 인생을 함께한 청춘들. 이들을 중심으로 다섯 가족의 따듯한 이야기가 펼쳐진다.")
                                                    .setImage(
                                                            new Image()
                                                                    .setUrl("https://actions.o2o.kr/devsvr1/image/응답하라 1988.jpg")
                                                                    .setAccessibilityText("응답하라 1988")
                                                    )
                                                    .setOptionInfo(
                                                            new OptionInfo()
                                                                    .setKey("2")
                                                    ),
                                            new ListSelectListItem()
                                                    .setTitle("괜찮아, 사랑이야")
                                                    .setDescription("인기 추리소설 작가이자 라디오 DJ 재열과 대학병원 정신과 교수 의사 해수. 만나기만 하면 티격태격하던 두 사람이 한집에 살게 되면서 변하기 시작한다.")
                                                    .setImage(
                                                            new Image()
                                                                    .setUrl("https://actions.o2o.kr/devsvr1/image/괜찮아, 사랑이야.jpg")
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
                                                    .setDescription("이번엔 누구? 한 달에 일주일, 다른 사람으로 사는 여자. 이 사람 누구? 열두 달 매일, 다른 사람 얼굴을 못 알아보는 남자. 남모를 속사정이 있는 남녀가 만났다. 서로의 비밀스러운 세계로 발을 디딘 둘의 로맨스는 어떤 모습일까.")
                                                    .setImage(
                                                            new Image()
                                                                    .setUrl("https://actions.o2o.kr/devsvr1/image/뷰티 인사이드.jpg")
                                                                    .setAccessibilityText("뷰티 인사이드")
                                                    )
                                                    .setOptionInfo(
                                                            new OptionInfo()
                                                                    .setKey("1")
                                                    ),
                                            new ListSelectListItem()
                                                    .setTitle("또! 오해영")
                                                    .setDescription("이름만 달랐어도 인생이 좀 나아졌을까? 학창 시절, 예쁘고 잘난 동명이인때문에 온갖 수난을 겪으며 살아온 여자 오해영. 이제는 만날 일 없다고 생각했지만 웬걸. 다시 나타난 예쁜 그녀가 해영의 삶을 또 한 번 망쳐놓을 줄이야!")
                                                    .setImage(
                                                            new Image()
                                                                    .setUrl("https://actions.o2o.kr/devsvr1/image/또! 오해영.jpg")
                                                                    .setAccessibilityText("또! 오해영")
                                                    )
                                                    .setOptionInfo(
                                                            new OptionInfo()
                                                                    .setKey("2")
                                                    ),
                                            new ListSelectListItem()
                                                    .setTitle("식샤를 합시다2")
                                                    .setDescription("1인 가구의 블루오션을 찾아 세종시에 온 보험왕 대영. 옆집 여자는 알고 보니 그에게 한을 품은 초등학교 동창 수지. 연애고자 수지와 상우를 이어주려고 식샤님이 출동한다.")
                                                    .setImage(
                                                            new Image()
                                                                    .setUrl("https://actions.o2o.kr/devsvr1/image/식샤를 합시다2.jpg")
                                                                    .setAccessibilityText("식샤를 합니디2")
                                                    )
                                                    .setOptionInfo(
                                                            new OptionInfo()
                                                                    .setKey("3")
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
                                                    .setDescription("1,000년을 거슬러 고려 시대로 시간 여행을 하게 된 21세기 여인. 궁중의 암투에 휘말리고 여러 황자들의 사랑을 한몸에 받게 된다.")
                                                    .setImage(
                                                            new Image()
                                                                    .setUrl("https://actions.o2o.kr/devsvr1/image/달의 연인 - 보보경심 려.jpg")
                                                                    .setAccessibilityText("달의 연인 - 보보경심 려")
                                                    )
                                                    .setOptionInfo(
                                                            new OptionInfo()
                                                                    .setKey("1")
                                                    ),
                                            new ListSelectListItem()
                                                    .setTitle("페르소나")
                                                    .setDescription("재능과 개성이 넘치는 4명의 감독, 그들이 만든 4편의 작품. 그 속에서 1명의 뮤즈가 4개의 페르소나로 변신한다. 때론 귀엽게 때론 묘하게, 삶과 사랑을 이야기한다.")
                                                    .setImage(
                                                            new Image()
                                                                    .setUrl("https://actions.o2o.kr/devsvr1/image/페르소나.jpg")
                                                                    .setAccessibilityText("페르소나")
                                                    )
                                                    .setOptionInfo(
                                                            new OptionInfo()
                                                                    .setKey("2")
                                                    ),
                                            new ListSelectListItem()
                                                    .setTitle("나의 아저씨")
                                                    .setDescription("순리대로 살지만 소년의 순수성과 어른의 지혜를 갖춘 아저씨가 있다. 그런데 이상한 애가 그를 흔든다. 거친 인생을 살아온 무모한 스물한 살 그녀가. 어느것 우정이 움트고, 둘은 서로에게 안식처가 된다. 이 차가운 세상에서.")
                                                    .setImage(
                                                            new Image()
                                                                    .setUrl("https://actions.o2o.kr/devsvr1/image/나의 아저씨.jpg")
                                                                    .setAccessibilityText("나의 아저씨")
                                                    )
                                                    .setOptionInfo(
                                                            new OptionInfo()
                                                                    .setKey("3")
                                                    )
                                    )
                            );
					break;
			}

		} else if(actor.equals("") && author.equals("")) { //genre 선택
			switch(genre) {
				case "로맨틱코미디":
                    selectionList
                            .setTitle("로맨틱 코미디 드라마 목록")
                            .setItems(
                                    Arrays.asList(
                                            new ListSelectListItem()
                                                    .setTitle("사이코지만 괜찮아")
                                                    .setDescription("소년은 오늘도 악몽에서 깨어났어요. 형이 좋아하는 동화 작가 문영이 강태가 일하는 병원에 낭독회를 하러 온다. 그리고 그녀가 다짜고짜 묻는다. 혹시, 운명을 믿어요?")
                                                    .setImage(
                                                            new Image()
                                                                    .setUrl("https://actions.o2o.kr/devsvr1/image/사이코지만 괜찮아.jpg")
                                                                    .setAccessibilityText("사이코지만 괜찮아")
                                                    )
                                                    .setOptionInfo(
                                                            new OptionInfo()
                                                                    .setKey("1")
                                                    ),
                                            new ListSelectListItem()
                                                    .setTitle("멜로가 체질")
                                                    .setDescription("스타 드라마 작가로 우뚝 설 그날만을 꿈꾸는 여자. 젊은 나이에 다큐멘터리 감독으로 성공한 여자. 일하느라 혼자 아들 키우느라 정신없이 살아가는 여자. 각기 다른 상황에서 일과 연애를 모두 잡으려 애쓰는 서른 살 그녀들의 이야기.")
                                                    .setImage(
                                                            new Image()
                                                                    .setUrl("https://actions.o2o.kr/devsvr1/image/멜로가 체질.jpg")
                                                                    .setAccessibilityText("멜로가 체질")
                                                    )
                                                    .setOptionInfo(
                                                            new OptionInfo()
                                                                    .setKey("2")
                                                    ),
                                            new ListSelectListItem()
                                                    .setTitle("로맨스는 별책부록")
                                                    .setDescription("책을 만들었는데 로맨스가 따라왔다? 잘나가는 스타 작가이자 출판계 역사를 새로 쓴 최연소 편집장. 그가 경력단절녀가 돼버린 전직 카피라이터의 인생에 깊이 뛰어든다.")
                                                    .setImage(
                                                            new Image()
                                                                    .setUrl("https://actions.o2o.kr/devsvr1/image/로맨스는 별책부록.jpg")
                                                                    .setAccessibilityText("로맨스는 별책부록")
                                                    )
                                                    .setOptionInfo(
                                                            new OptionInfo()
                                                                    .setKey("3")
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
                                                    .setTitle("나 홀로 그대")
                                                    .setDescription("사람이야, 귀신이야? 우연히 홀로그램 인공 지능 '홀로'를 손에 넣게 된 소연. 처음엔 무서웠던 그가 급속도로 편해진다. 내 눈에만 보이는 친구라니, 덜 외롭고 좋다.")                                                    .setImage(
                                                            new Image()
                                                                    .setUrl("https://actions.o2o.kr/devsvr1/image/나 홀로 그대.jpg")
                                                                    .setAccessibilityText("나 홀로 그대")
                                                    )
                                                    .setOptionInfo(
                                                            new OptionInfo()
                                                                    .setKey("1")
                                                    ),
                                            new ListSelectListItem()
                                                    .setTitle("알함브라 궁전의 추억")
                                                    .setDescription("저돌적인 투자회사 대표 진우. 익명의 AR 게임 개발자를 찾던 그는 그라나다까지 온다. 그 곳에서 호스텔을 운영하는 희주. 둘이 만난 순간, 마법같은 이야기가 시작된다.")
                                                    .setImage(
                                                            new Image()
                                                                    .setUrl("https://actions.o2o.kr/devsvr1/image/알함브라 궁전의 추억.jpg")
                                                                    .setAccessibilityText("알함브라 궁전의 추억")
                                                    )
                                                    .setOptionInfo(
                                                            new OptionInfo()
                                                                    .setKey("2")
                                                    ),
                                            new ListSelectListItem()
                                                    .setTitle("어비스")
                                                    .setDescription("서로 다른 사고로 이른 나이게 죽은 두 남녀. 그런데 두 사람이 완전히 달라진 외모로 살아난다. 그리고 그들 앞에 닥친 새로운 목표. 자신을 죽인 살인마를 찾아야 한다.")
                                                    .setImage(
                                                            new Image()
                                                                    .setUrl("https://actions.o2o.kr/devsvr1/image/어비스.jpg")
                                                                    .setAccessibilityText("어비스")
                                                    )
                                                    .setOptionInfo(
                                                            new OptionInfo()
                                                                    .setKey("3")
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
                                                    .setTitle("보이스")
                                                    .setDescription("소리에서 단서를 찾는 '보이스 프로파일러'의 세계. 112 신고센터장 구너주와 열혈 형사 진혁이 생사의 갈림길에 선 사람들을 구하기 위해 작은 소리 하나도 놓치지 않는다. 골든타임의 경계를 넘나드는 그들의 활약. 오늘도 출동이다.")
                                                    .setImage(
                                                            new Image()
                                                                    .setUrl("https://actions.o2o.kr/devsvr1/image/보이스.jpg")
                                                                    .setAccessibilityText("보이스")
                                                    )
                                                    .setOptionInfo(
                                                            new OptionInfo()
                                                                    .setKey("1")
                                                    ),
                                            new ListSelectListItem()
                                                    .setTitle("인간수업")
                                                    .setDescription("눈에 띄지 않게 살자. 학교 안에서는 성실하고 모범적인 오지수. 학교 밖에서는 전혀 다른 얼굴로 살고 있다. 그가 위험한 불법 사업의 중심에 있다. 아무도 모르게.")
                                                    .setImage(
                                                            new Image()
                                                                    .setUrl("https://actions.o2o.kr/devsvr1/image/인간수업.jpg")
                                                                    .setAccessibilityText("인간수업")
                                                    )
                                                    .setOptionInfo(
                                                            new OptionInfo()
                                                                    .setKey("2")
                                                    ),
                                            new ListSelectListItem()
                                                    .setTitle("비밀의 숲")
                                                    .setDescription("공감 능력을 잃은 검사가 살인 사건을 파헤친다. 검사는 열혈 여형사의 도움을 받으며, 수사를 방해하는 부패한 정치 상황에 맞선다.")
                                                    .setImage(
                                                            new Image()
                                                                    .setUrl("https://actions.o2o.kr/devsvr1/image/비밀의 숲.jpg")
                                                                    .setAccessibilityText("비밀의 숲")
                                                    )
                                                    .setOptionInfo(
                                                            new OptionInfo()
                                                                    .setKey("3")
                                                    )
                                    )
                            );
					break;
			}
		} else if(actor.equals("") && genre.equals("")){ //author 선택
		    switch (author){
                case "김은희":
                    selectionList
                            .setTitle("김은희 작가의 드라마 목록")
                            .setItems(
                                    Arrays.asList(
                                            new ListSelectListItem()
                                                    .setTitle("시그널")
                                                    .setDescription("과거와 현재를 잇는 무전기를 통해 대화르 나누는 1989년의 형사와 2015년의 프로파일러. 그리고 두 사람 모두와 연결되어 있는 또 한 명의 형사. 사건을 해결하고 정의를 실현하기 위한 기묘한 공조 수사가 시작된다.")
                                                    .setImage(
                                                            new Image()
                                                                    .setUrl("https://actions.o2o.kr/devsvr1/image/시그널.jpg")
                                                                    .setAccessibilityText("시그널")
                                                    )
                                                    .setOptionInfo(
                                                            new OptionInfo()
                                                                    .setKey("1")
                                                    ),
                                            new ListSelectListItem()
                                                    .setTitle("킹덤")
                                                    .setDescription("병든 왕을 둘러싸고 흉흉한 소문이 떠돈다. 어둠에 뒤덮인 조선, 기이한 역병에 신음하는 산하. 정체 모를 악에 맞서 백성을 구원할 희망을 오직 세자뿐이다.")
                                                    .setImage(
                                                            new Image()
                                                                    .setUrl("https://actions.o2o.kr/devsvr1/image/킹덤.jpg")
                                                                    .setAccessibilityText("킹덤")
                                                    )
                                                    .setOptionInfo(
                                                            new OptionInfo()
                                                                    .setKey("2")
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
                                                    .setTitle("미스터션샤인")
                                                    .setDescription("1871년, 한 소년이 미국 군함을 타고 조선을 떠난다. 세월이 흐르고, 미군 장교가 되어 조국으로 돌아온 남자. 격변하는 역사 속에서, 그는 운명을 뒤흔들 여인을 만난다.")
                                                    .setImage(
                                                            new Image()
                                                                    .setUrl("https://actions.o2o.kr/devsvr1/image/미스터션샤인.jpg")
                                                                    .setAccessibilityText("미스터션샤인")
                                                    )
                                                    .setOptionInfo(
                                                            new OptionInfo()
                                                                    .setKey("1")
                                                    ),
                                            new ListSelectListItem()
                                                    .setTitle("더 킹 - 영원의 군주")
                                                    .setDescription("이림에 대한 단서를 찾는 이곤. 만약을 대비해, 영을 두고 은섭을 데리고 돌아간다. 과거 기억으로 혼란스러운 신재, 우편물 발신자가 궁금한 서령. 대체 진실은 무엇일까.")
                                                    .setImage(
                                                            new Image()
                                                                    .setUrl("https://actions.o2o.kr/devsvr1/image/더 킹 - 영원의 군주.jpg")
                                                                    .setAccessibilityText("더 킹 - 영원의 군주")
                                                    )
                                                    .setOptionInfo(
                                                            new OptionInfo()
                                                                    .setKey("2")
                                                    ),
                                            new ListSelectListItem()
                                                    .setTitle("도깨비")
                                                    .setDescription("신부를 찾아야 죽을 수 있는 남자. 불멸의 고통에 힘겹던 어느 날, 신부라고 주장하는 여학생이 나타났다. 대책 없이 그를 소환하고 대책 없이 삶에 파고드는 소녀. 정녕 신부를 만난 것이냐. 그럼 이제 소멸할 수 있는 것이냐.")
                                                    .setImage(
                                                            new Image()
                                                                    .setUrl("https://actions.o2o.kr/devsvr1/image/도깨비.jpg")
                                                                    .setAccessibilityText("도깨비")
                                                    )
                                                    .setOptionInfo(
                                                            new OptionInfo()
                                                                    .setKey("3")
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
                                                    .setDescription("자식들 뒷바리지하랴, 가족들 건사하랴, 어느새 축 지나가 버린 세월. 그렇게 노년에 접어들었지만 인생, 아직 저물지 않았다. '시니어벤저스'와 노희경 표 반짝이는 명대사의 만남, 꼰대들의 유쾌한 인생 찬가가 펼쳐진다.")
                                                    .setImage(
                                                            new Image()
                                                                    .setUrl("https://actions.o2o.kr/devsvr1/image/디어 마이 프렌즈.jpg")
                                                                    .setAccessibilityText("디어 마이 프렌즈")
                                                    )
                                                    .setOptionInfo(
                                                            new OptionInfo()
                                                                    .setKey("1")
                                                    ),
                                            new ListSelectListItem()
                                                    .setTitle("그 겨울, 바람이 분다")
                                                    .setDescription("여자, 술, 포커가 인생의 전부인 사기꾼. 시각장애를 가진 재벌가 상속녀에세 접근해 한몫 챙길 심산이다. 하지만 자기를 닮은 그녀의 모습에 미묘한 감정을 느끼고, 계획에서 멀저지기 시작한다. 자신의 목숨이 걸린 일이란 것도 잊은 채.")
                                                    .setImage(
                                                            new Image()
                                                                    .setUrl("https://actions.o2o.kr/devsvr1/image/그 겨울, 바람이 분다.jpg")
                                                                    .setAccessibilityText("그 겨울, 바람이 분다")
                                                    )
                                                    .setOptionInfo(
                                                            new OptionInfo()
                                                                    .setKey("2")
                                                    ),
                                            new ListSelectListItem()
                                                    .setTitle("라이브")
                                                    .setDescription("공권력의 상징 대한민국 경찰. 하지만 이들도 알고 보면 우리와 닮은 이웃일 뿐. 제복의 무게를 견디며 오늘도 정의를 위해 출동이다! 특별하고도 평범한 삶을 위하여.")
                                                    .setImage(
                                                            new Image()
                                                                    .setUrl("https://actions.o2o.kr/devsvr1/image/라이브.jpg")
                                                                    .setAccessibilityText("라이브")
                                                    )
                                                    .setOptionInfo(
                                                            new OptionInfo()
                                                                    .setKey("3")
                                                    )
                                    )
                            );
                    break;
            }
        }

//		ArrayList<ListSelectListItem> list = new ArrayList<>();
//
//		for(int i = 0; i < dramaList.items.size(); i++) {
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
//		}
//
//		selectionList
//				.setTitle("선택한 드라마 목록")
//				.setItems(list);

		simpleResponse.setTextToSpeech("어떤 드라마를 선택하시겠어요?")
				.setDisplayText("원하는 드라마를 선택해주세요.");

		responseBuilder.add(selectionList);
		responseBuilder.add(simpleResponse);

		return responseBuilder.build();
	}

//	/**
//	 * 나라별 드라마 목록을 가져오는 api 처리
//	 * @param countries
//	 * @return
//	 */
//	private void processCountries(String countries) {
//		JsonParser jsonParser = new JsonParser(); ApiController apiController = new ApiController();
//		JsonObject jsonobj = (JsonObject) jsonParser.parse(apiController.getCountriesList(countries));
//		JsonArray results = jsonobj.get("ITEMS").getAsJsonArray();
////		dramaList.items.clear();
//		for(int i = 0; i < 8; i++){
//			Drama drama = new Drama();
//			JsonObject element = results.get(i).getAsJsonObject();
//			drama.title = element.get("title").toString();
//			drama.imgUrl = element.get("image").toString();
//			drama.id = element.get("netflixid").toString();
//			drama.synopsis = element.get("synopsis").toString();
//			drama.titleDate = element.get("unogsdate").toString();
//			drama.avgrating = element.get("rating").toString();
//			dramaList.items.add(drama);
//		}
//	}
//
//	private String processTranslate(String text) {
//		Translate translate = TranslateOptions.getDefaultInstance().getService();
//
//		Translation translation = translate.translate(
//				text,
//				Translate.TranslateOption.sourceLanguage("en"),
//				Translate.TranslateOption.targetLanguage("ko")
//				);
//
//		return translation.getTranslatedText();
//	}
//
//	/**
//	 * 장르별 드라마를 가져오는 api 처리
//	 * @param id
//	 */
//	private void processGenre(int id) {
//		JsonParser jsonParser = new JsonParser(); ApiController apiController = new ApiController();
//		JsonObject jsonobj = (JsonObject) jsonParser.parse(apiController.getGenreList(id));
//		JsonArray results = jsonobj.get("ITEMS").getAsJsonArray();
////		dramaList.items.clear();
//		for(int i = 0; i < 8; i++){
//			Drama drama = new Drama();
//			JsonObject element = results.get(i).getAsJsonObject();
//			drama.title = element.get("title").toString();
//			drama.imgUrl = element.get("image").toString();
//			drama.id = element.get("netflixid").toString();
//			drama.synopsis = element.get("synopsis").toString();
//			drama.titleDate = element.get("unogsdate").toString();
//			drama.avgrating = element.get("rating").toString();
//			dramaList.items.add(drama);
//		}
//	}

    int netflixId;
	/**
	 * 선택한 드라마에 대한 설명을 출력하는 인텐트
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

        String selectedItem = request.getSelectedOption();
        String drama = CommonUtil.makeSafeString(request.getParameter("drama"));

        if (selectedItem.equals("1")) {
            drama = "You selected the first item";
        } else if (selectedItem.equals("2")) {
            drama = "You selected the Second item";
        } else if (selectedItem.equals("3")) {
            drama = "You selected the Third item";
        } else {
            drama = "You did not select a valid item";
        }

//        int i;
//        for(i = 0; i < dramaList.items.size(); i++){
//        	if(selectedItem.equals(dramaList.items.get(i).id)) {
//				drama = "You selected the" + i + "item";
//				id = dramaList.items.get(i).id;
//			}
//        	else
//        		drama = CommonUtil.makeSafeString(request.getParameter("drama"));
//		}


		SimpleResponse simpleResponse = new SimpleResponse();
		SimpleResponse simpleResponse2 = new SimpleResponse();
        BasicCard basicCard = new BasicCard();

        simpleResponse2
                .setTextToSpeech("네, " + drama + "에 대해 알려드릴게요");

        String synopsis = new String();
        String[] actors = new String[] {};
        switch(drama){
            case "라이브":
                synopsis = "전국에서 제일 바쁜 \'홍일 지구대\'에 근무하며 일상의 소소한 가치와 정의를 지키기 위해 밤낮없이 바쁘게 뛰며 사건을 해결하는 지구대 경찰들의 이야기";
                actors = new String[]{"정유미", "이광수", "성동일"};
                netflixId = 80214523;
                break;
            case "응답하라 1988":
                synopsis = "쌍팔년도 쌍문동, 한 골목 다섯 가족의 왁자지껄 코믹 가족극";
                actors = new String[] {"성동일", "이일화", "라미란"};
                netflixId = 80188351;
                break;
            case "괜찮아, 사랑이야":
                synopsis = "작은 외상에는 병적으로 집착하며 호들갑을 떨지만 마음의 병은 짊어지고 살아가는 현대인들의 삶과 사랑을 되짚어보는 이야기";
                actors = new String[] {"조인성", "공효진", "성동일"};
                netflixId = 80031632;
                break;
            case "또! 오해영":
                synopsis = "\"모든 것은 오해로 시작되었다!\" '오해영'이라는 동명이인의 두 여자와 그들 사이에서 미래를 보기 시작한 남자 '박도경'이 미필적 고의로 서로의 인생에 얽혀가는 동명 오해 로맨스";
                actors = new String[] {"에릭", "서현진", "전해빈"};
                netflixId = 81077044;
                break;
            case "뷰티 인사이드":
                synopsis = "한 달에 일주일 타인의 얼굴로 살아가는 여자와 일 년 열두 달 타인의 얼굴을 알아보지 못하는 남자의 조금은 특별한 로맨스를 그린 드라마";
                actors = new String[] {"서현진", "이민기", "이다희"};
                netflixId = 81029990;
                break;
            case "식샤를 합시다2":
                synopsis = "구대영의 적수가 나타났다! 맛집 블로거 식샤님 '구대영'과 그를 원수로 기억하는 '1일1식 다이어트' 4년차 프리랜서 작가 '백수지', 초식남 공무원 '이상우'. 입맛 다른 '세종 빌라' 1인 가구들의 매콤하게 맛있는 드라마";
                actors = new String[] {"윤두준", "서현진", "권율"};
                netflixId = 80183878;
                break;
            case "달의 연인 - 보보경심 려":
                synopsis = "달그림자가 태양을 검게 물들인 날. 상처 입은 짐승 같은 사내, 4황자 '왕소'와 21세기 여인 '고하진'의 영혼이 미끄러져 들어간 고려 소녀 '해수'가 천 년의 시공간을 초월해 만난다.";
                actors = new String[] {"이준기", "이지은", "강하늘"};
                netflixId = 80156759;
                break;
            case "페르소나":
                synopsis = "이경미, 임필성, 전고운, 김종관 4명의 감독이 페르소나 이지은을 각기 다른 시선으로 풀어낸 총 4편의 오리지널 시리즈";
                actors = new String[] {"이지은", "배두나", "박해수"};
                netflixId = 81044884;
                break;
            case "나의 아저씨":
                synopsis = "삶의 무게를 버티며 살아가는 아저씨 삼 형제와 거칠게 살아온 한 여성이 서로를 통해 삶을 치유하게 되는 이야기";
                actors = new String[] {"이선균", "이지은", "고두심"};
                netflixId = 81267691;
                break;
            case "사이코지만 괜찮아":
                synopsis = "버거운 삶의 무게로 사랑을 거부하는 정신 병동 보호사 강태와 태생적 결함으로 사랑을 모르는 동화 작가 문영이 서로의 상처를 보듬고 치유해가는 한 편의 판타지 동화 같은 사랑에 관한 조금 이상한 로맨틱 코미디";
                actors = new String[] {"김수현", "서예지", "오정세"};
                netflixId = 81243992;
                break;
            case "멜로가 체질":
                synopsis = "서른 살 여자 친구들의 고민, 연애, 일상을 그린 코믹 드라마";
                actors = new String[] {"천우희", "전여빈", "한지은"};
                netflixId = 81211284;
                break;
            case "로맨스는 별책부록":
                synopsis = "'책을 만들었는데, 로맨스가 따라왔다?' 책을 읽지 않는 세상에서 책을 만드는 사람들의 이야기를 담은 로맨틱 코미디 드라마";
                actors = new String[] {"이나영", "이종석", "정유진"};
                netflixId = 81045349;
                break;
            case "나 홀로 그대":
                synopsis = "남모를 아픔을 숨기기 위해 스스로 외톨이가 된 소연과 다정하고 완벽한 인공지능 비서 홀로, 그와 얼굴은 같지만 성격은 정반대인 개발자 난도가 서로를 만나, 사랑할수록 외로워지는 불완전한 로맨스를 그리는 넷플릭스 오리지널 시리즈";
                actors = new String[] {"윤현민", "고성희", "최여진"};
                netflixId = 81008021;
                break;
            case "알함브라 궁전의 추억":
                synopsis = "투자회사 대표인 남자주인공이 비즈니스로 스페인 그라나다에 갔다가 전직 기타리스트였던 여주인공이 운영하는 싸구려 호스텔에 묵으며 두 사람이 기묘한 사건에 휘말리며 펼쳐지는 이야기";
                actors = new String[] {"현빈", "박신혜", "박훈"};
                netflixId = 81004280;
                break;
            case "어비스":
                synopsis = "\"영혼 소생 구슬\" 어비스를 통해 생전과 180도 다른 '반전 비주얼'로 부활한 두 남녀가 자신을 죽인 살인자를 쫓는 반전 비주얼 판타지";
                actors = new String[] {"박보영", "안효섭", "이성재"};
                netflixId = 81087762;
                break;
            case "보이스":
                synopsis = "범죄 현장의 골든타임을 사수하는 112 신고센터 대원들의 치열한 기록을 그린 소리 추격 스릴러 드라마";
                actors = new String[] {"이하나", "장혁", "이진욱"};
                netflixId = 80987095;
                break;
            case "인간수업":
                synopsis = "돈을 벌기 위해 죄책감없이 범죄의 길을 선택한 고등학생들이 그로 인해 돌이킬 수 없이 혹독한 대가를 치르는 과정을 그린 넷플릭스 오리지널 시리즈";
                actors = new String[] {"김동희", "정다빈", "박주현"};
                netflixId = 80990668;
                break;
            case "비밀의 숲":
                synopsis = "감정을 느끼지 못하는 외톨이 검사 황시목이, 정의롭고 따뜻한 형사 한여진과 함께 검찰 스폰서 살인사건과 그 이면에 숨겨진 진실을 파헤치는 내부 비밀 추적극";
                actors = new String[] {"조승우", "배두나", "이준혁"};
                netflixId = 80187302;
                break;
            case "시그널":
                synopsis = "\"우리의 시간은 이어져있다.\" 과거로부터 걸려온 간절한 신호(무전)로 연결된 현재와 과거의 형사들이 오래된 미제 사건들을 다시 파헤친다!";
                actors = new String[] {"이제훈", "김혜수", "조진웅"};
                netflixId = 80987077;
                break;
            case "킹덤":
                synopsis = "죽은 자들이 살아나 생지옥이 된 위기의 조선, 왕권을 탐하는 조씨 일가의 탐욕과 누구도 믿을 수 없게 되어버린 왕세자 창의 피의 사투를 그린 미스터리 스릴러";
                actors = new String[] {"주지훈", "류승룡", "배두나"};
                netflixId = 80180171;
                break;
            case "미스터션샤인":
                synopsis = "신미양요(1871년) 때 군함에 승선해 미국에 떨어진 한 소년이 미국 군인 신분으로 자신을 버린 조국인 조선으로 돌아와 주둔하며 벌어지는 일을 그린 드라마";
                actors = new String[] {"이병헌", "김태리", "유연석"};
                netflixId = 80991107;
                break;
            case "더 킹 - 영원의 군주":
                synopsis = "악마에 맞서 차원의 문(門)을 닫으려는 이과(理科)형 대한제국 황제와 누군가의 삶·사람·사랑을 지키려는 문과(文科)형 대한민국 형사의 공조를 통해 차원이 다른 로맨스를 그린 드라마";
                actors = new String[] {"이민호", "김고은", "우도환"};
                netflixId = 81260283;
                break;
            case "도깨비":
                synopsis = "불멸의 삶을 끝내기 위해 인간 신부가 필요한 도깨비, 그와 기묘한 동거를 시작한 기억상실증 저승사자. 그런 그들 앞에 '도깨비 신부'라 주장하는 '죽었어야 할 운명'의 소녀가 나타나며 벌어지는 신비로운 낭만 설화";
                actors = new String[] {"공유", "김고은", "이동욱"};
                netflixId = 81012510;
                break;
            case "디어 마이 프렌즈":
                synopsis = "\"끝나지 않았다. 여전히 살아있다\"고 외치는 '황혼 청춘'들의 인생 찬가를 그린 드라마";
                actors = new String[] {"고현정", "김혜자", "고두심"};
                netflixId = 81267633;
                break;
            case "그 겨울, 바람이 분다":
                synopsis = "유년시절 부모로부터 버려지고 첫사랑에 실패한 후 의미 없는 삶을 사는 남자와 부모의 이혼과 오빠와의 결별, 갑자기 찾아온 시각 장애로 외롭고 고단한 삶을 사는 여자가 만나 차갑고 외로웠던 그들의 삶에서 희망과 진정한 사랑의 의미를 찾아가는 이야기";
                actors = new String[] {"조인성", "송혜교", "김범"};
                netflixId = 70296735;
                break;
        }


		simpleResponse2
				.setTextToSpeech("네, " + drama + "에 대해 알려드릴게요");

//		String synopsis = "줄거리 " + dramaList.items.get(i).synopsis + "\n";
//		String titleDate = "방영일" + dramaList.items.get(i).titleDate + "\n";
//		String avgrating = "평점" + dramaList.items.get(i).avgrating + "\n";

		//해당 드라마의 포스터와 간단 줄거리 등을 보여줌
		basicCard
				.setTitle(drama)
				.setImage(new Image().setUrl("https://actions.o2o.kr/devsvr1/image/" + drama + ".jpg")
						.setAccessibilityText(drama))
				.setFormattedText(synopsis + actors);

		simpleResponse
				.setTextToSpeech("시청하시겠어요?")
				.setDisplayText("시청하시겠어요?");

		responseBuilder.add(simpleResponse2);
		responseBuilder.add(basicCard);
		responseBuilder.add(simpleResponse);

		return responseBuilder.build();
	}

	/**
	 * 드라마를 시청하면 넷플릭스에 연결하는 인텐트
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
		String id;


		simpleResponse
				.setTextToSpeech("넷플릭스를 연결할게요");

		linkOutSuggestion
				.setDestinationName("넷플릭스 연결")
				.setUrl("https://www.netflix.com/title/" + netflixId);

		responseBuilder.add(simpleResponse);
		responseBuilder.add(linkOutSuggestion);

		return responseBuilder.build();
	}

	/**
	 * 드라마를 시청하지 않는다고 하면 처음으로 돌아가는 인텐트
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
				.setTextToSpeech("장르, 배우 혹은 작가 중 하나를 선택해주세요.");

		suggestions.add("장르");
		suggestions.add("배우");
		suggestions.add("작가");

		responseBuilder.add(simpleResponse);
		responseBuilder.add(simpleResponse2);
		responseBuilder.addSuggestions(suggestions.toArray(new String[suggestions.size()]));

		return responseBuilder.build();
	}
}

