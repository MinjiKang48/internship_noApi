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
    int netflixId;

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

    // you can add a fallback function instead of a function for individual intents
    @ForIntent("Default Fallback Intent")
    public ActionResponse fallback(ActionRequest request) {
        final String WELCOME_INTENT = "Default Welcome Intent";
        final String CHOICE_INTENT = "choice";
        final String DESCRIPTION_INTENT = "dramaDescription";
        // intent contains the name of the intent
        // you defined in the Intents area of Dialogflow
        ResponseBuilder responseBuilder = getResponseBuilder(request);
        SimpleResponse simpleResponse = new SimpleResponse();
        SelectionList selectionList = new SelectionList();
        List<String> suggestions = new ArrayList<String>();
        BasicCard basicCard = new BasicCard();
        String intent = request.getIntent();
        switch (intent) {
            case WELCOME_INTENT:
                responseBuilder.add("죄송해요. 다시 들려 주실래요?");
                break;
            case CHOICE_INTENT:
                responseBuilder.add("장르, 배우, 작가 중 하나를 선택해주세요.");

                String choice = CommonUtil.makeSafeString(request.getParameter("choice"));

                if (choice.equals("장르")) { //장르 선택 (문제 발생)
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
                    suggestions.add("로맨틱코미디");
                } else if (choice.equals("배우")) { //배우 선택
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
                } else if (choice.equals("작가")) { //작가 선택
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
                break;
            case DESCRIPTION_INTENT:
                responseBuilder.add("목록에 있는 드라마를 선택해주세요");

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
                                                            .setDescription("공권력의 상징 대한민국 경찰. 하지만 이들도 알고 보면 우리와 닮은 이웃일 뿐. 제복의 무게를 견디며 오늘도 정의를 위해 출동이다! 특별하고도 평범한 삶을 위하여.")
                                                            .setImage(
                                                                    new Image()
                                                                            .setUrl("https://actions.o2o.kr/devsvr1/image/라이브.jpg")
                                                                            .setAccessibilityText("라이브")
                                                            )
                                                            .setOptionInfo(
                                                                    new OptionInfo()
                                                                            .setKey("라이브")
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
                                                                            .setKey("응팔")
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
                                                                            .setKey("괜사")
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
                                                                            .setKey("뷰인사")
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
                                                                            .setKey("오해영")
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
                                                                            .setKey("식샤")
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
                                                                            .setKey("보보경심")
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
                                                                            .setKey("페르소나")
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
                                                                            .setKey("나저씨")
                                                            )
                                            )
                                    );
                            break;
                    }

                } else if (actor.equals("") && author.equals("")) { //genre 선택
                    switch (genre) {
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
                                                                            .setKey("사이코")
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
                                                                            .setKey("멜체")
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
                                                                            .setKey("로별")
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
                                                            .setDescription("사람이야, 귀신이야? 우연히 홀로그램 인공 지능 '홀로'를 손에 넣게 된 소연. 처음엔 무서웠던 그가 급속도로 편해진다. 내 눈에만 보이는 친구라니, 덜 외롭고 좋다.").setImage(
                                                            new Image()
                                                                    .setUrl("https://actions.o2o.kr/devsvr1/image/나 홀로 그대.jpg")
                                                                    .setAccessibilityText("나 홀로 그대")
                                                    )
                                                            .setOptionInfo(
                                                                    new OptionInfo()
                                                                            .setKey("나홀로")
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
                                                                            .setKey("알함브라")
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
                                                                            .setKey("어비스")
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
                                                                            .setKey("보이스")
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
                                                                            .setKey("인간수업")
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
                                                                            .setKey("비숲")
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
                                                            .setDescription("과거와 현재를 잇는 무전기를 통해 대화르 나누는 1989년의 형사와 2015년의 프로파일러. 그리고 두 사람 모두와 연결되어 있는 또 한 명의 형사. 사건을 해결하고 정의를 실현하기 위한 기묘한 공조 수사가 시작된다.")
                                                            .setImage(
                                                                    new Image()
                                                                            .setUrl("https://actions.o2o.kr/devsvr1/image/시그널.jpg")
                                                                            .setAccessibilityText("시그널")
                                                            )
                                                            .setOptionInfo(
                                                                    new OptionInfo()
                                                                            .setKey("시그널")
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
                                                                            .setKey("킹덤")
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
                                                            .setDescription("1871년, 한 소년이 미국 군함을 타고 조선을 떠난다. 세월이 흐르고, 미군 장교가 되어 조국으로 돌아온 남자. 격변하는 역사 속에서, 그는 운명을 뒤흔들 여인을 만난다.")
                                                            .setImage(
                                                                    new Image()
                                                                            .setUrl("https://actions.o2o.kr/devsvr1/image/미스터 션샤인.jpg")
                                                                            .setAccessibilityText("미스터 션샤인")
                                                            )
                                                            .setOptionInfo(
                                                                    new OptionInfo()
                                                                            .setKey("미션")
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
                                                                            .setKey("더킹")
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
                                                                            .setKey("도깨비")
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
                                                                            .setKey("디마프")
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
                                                                            .setKey("그겨울")
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
                                                                            .setKey("라이브")
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

        }
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
    @ForIntent("choice")
    public ActionResponse choice(ActionRequest request) throws ExecutionException, InterruptedException {
        ResponseBuilder responseBuilder = getResponseBuilder(request);
        Map<String, Object> data = responseBuilder.getConversationData();

        data.clear();

        List<String> suggestions = new ArrayList<String>();
        SimpleResponse simpleResponse = new SimpleResponse();
        BasicCard basicCard = new BasicCard();

        String choice = CommonUtil.makeSafeString(request.getParameter("choice"));

        if (choice.equals("장르")) { //장르 선택 (문제 발생)
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
            suggestions.add("로맨틱코미디");
        } else if (choice.equals("배우")) { //배우 선택
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
        } else if (choice.equals("작가")) { //작가 선택
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
     * 선택한 장르 혹은 국가에 맞는 드라마 목록을 생성하는 인텐트
     *
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

        if (genre.equals("") && author.equals("")) { //actor 선택
            switch (actor) {
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
                                                                    .setKey("라이브")
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
                                                                    .setKey("응팔")
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
                                                                    .setKey("괜사")
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
                                                                    .setKey("뷰인사")
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
                                                                    .setKey("오해영")
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
                                                                    .setKey("식샤")
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
                                                                    .setKey("보보경심")
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
                                                                    .setKey("페르소나")
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
                                                                    .setKey("나저씨")
                                                    )
                                    )
                            );
                    break;
            }

        } else if (actor.equals("") && author.equals("")) { //genre 선택
            switch (genre) {
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
                                                                    .setKey("사이코")
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
                                                                    .setKey("멜체")
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
                                                                    .setKey("로별")
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
                                                    .setDescription("사람이야, 귀신이야? 우연히 홀로그램 인공 지능 '홀로'를 손에 넣게 된 소연. 처음엔 무서웠던 그가 급속도로 편해진다. 내 눈에만 보이는 친구라니, 덜 외롭고 좋다.").setImage(
                                                    new Image()
                                                            .setUrl("https://actions.o2o.kr/devsvr1/image/나 홀로 그대.jpg")
                                                            .setAccessibilityText("나 홀로 그대")
                                            )
                                                    .setOptionInfo(
                                                            new OptionInfo()
                                                                    .setKey("나홀로")
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
                                                                    .setKey("알함브라")
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
                                                                    .setKey("어비스")
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
                                                                    .setKey("보이스")
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
                                                                    .setKey("인간수업")
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
                                                                    .setKey("비숲")
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
                                                    .setDescription("과거와 현재를 잇는 무전기를 통해 대화르 나누는 1989년의 형사와 2015년의 프로파일러. 그리고 두 사람 모두와 연결되어 있는 또 한 명의 형사. 사건을 해결하고 정의를 실현하기 위한 기묘한 공조 수사가 시작된다.")
                                                    .setImage(
                                                            new Image()
                                                                    .setUrl("https://actions.o2o.kr/devsvr1/image/시그널.jpg")
                                                                    .setAccessibilityText("시그널")
                                                    )
                                                    .setOptionInfo(
                                                            new OptionInfo()
                                                                    .setKey("시그널")
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
                                                                    .setKey("킹덤")
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
                                                    .setDescription("1871년, 한 소년이 미국 군함을 타고 조선을 떠난다. 세월이 흐르고, 미군 장교가 되어 조국으로 돌아온 남자. 격변하는 역사 속에서, 그는 운명을 뒤흔들 여인을 만난다.")
                                                    .setImage(
                                                            new Image()
                                                                    .setUrl("https://actions.o2o.kr/devsvr1/image/미스터 션샤인.jpg")
                                                                    .setAccessibilityText("미스터 션샤인")
                                                    )
                                                    .setOptionInfo(
                                                            new OptionInfo()
                                                                    .setKey("미션")
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
                                                                    .setKey("더킹")
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
                                                                    .setKey("도깨비")
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
                                                                    .setKey("디마프")
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
                                                                    .setKey("그겨울")
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
                                                                    .setKey("라이브")
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

        SimpleResponse simpleResponse = new SimpleResponse();
        SimpleResponse simpleResponse2 = new SimpleResponse();
        BasicCard basicCard = new BasicCard();

        String drama = CommonUtil.makeSafeString(request.getParameter("drama"));

        String selectedItem = request.getSelectedOption();

        if (selectedItem.equals("라이브") || drama.equals("라이브")) {
            simpleResponse2
                    .setTextToSpeech("네, 라이브에 대해 알려드릴게요 " + drama);
            String synopsis = "전국에서 제일 바쁜 \'홍일 지구대\'에 근무하며 일상의 소소한 가치와 정의를 지키기 위해 밤낮없이 바쁘게 뛰며 사건을 해결하는 지구대 경찰들의 이야기";
            String actors = "정유미, 이광수, 성동일";
            basicCard
                    .setTitle("라이브")
                    .setImage(new Image().setUrl("https://actions.o2o.kr/devsvr1/image/라이브.jpg")
                            .setAccessibilityText("라이브"))
                    .setFormattedText("줄거리 : " + synopsis + "\n 배우 : " + actors);
            netflixId = 80214523;
        } else if (selectedItem.equals("응팔") || drama.equals("응답하라 1988")) {
            simpleResponse2
                    .setTextToSpeech("네, 응답하라 1988에 대해 알려드릴게요");
            String synopsis = "쌍팔년도 쌍문동, 한 골목 다섯 가족의 왁자지껄 코믹 가족극";
            String actors = "성동일, 이일화, 라미란";
            basicCard
                    .setTitle("응답하라 1988")
                    .setImage(new Image().setUrl("https://actions.o2o.kr/devsvr1/image/응답하라 1988.jpg")
                            .setAccessibilityText("응답하라 1988"))
                    .setFormattedText(synopsis + actors);
            netflixId = 80188351;
        } else if (selectedItem.equals("괜사") || drama.equals("괜찮아, 사랑이야")) {
            simpleResponse2
                    .setTextToSpeech(drama + "선택한 \'괜찮아, 사랑이야\'에요");
            String synopsis = "작은 외상에는 병적으로 집착하며 호들갑을 떨지만 마음의 병은 짊어지고 살아가는 현대인들의 삶과 사랑을 되짚어보는 이야기";
            String actors = "조인성, 공효진, 성동일";
            basicCard
                    .setTitle("괜찮아, 사랑이야")
                    .setImage(new Image().setUrl("https://actions.o2o.kr/devsvr1/image/괜찮아, 사랑이야.jpg")
                            .setAccessibilityText("괜찮아, 사랑이야"))
                    .setFormattedText(synopsis + actors);
            netflixId = 80031632;
        } else if (selectedItem.equals("오해영") || drama.equals("또! 오해영")) {
            simpleResponse2
                    .setTextToSpeech("네, 또! 오해영에 대해 알려드릴게요");
            String synopsis = "\"모든 것은 오해로 시작되었다!\" \'오해영\'이라는 동명이인의 두 여자와 그들 사이에서 미래를 보기 시작한 남자 '박도경'이 미필적 고의로 서로의 인생에 얽혀가는 동명 오해 로맨스";
            String actors = "에릭, 서현진, 전해빈";
            basicCard
                    .setTitle("또! 오해영")
                    .setImage(new Image().setUrl("https://actions.o2o.kr/devsvr1/image/또! 오해영.jpg")
                            .setAccessibilityText("또! 오해영"))
                    .setFormattedText(synopsis + actors);
            netflixId = 81077044;
        } else if (selectedItem.equals("뷰인사") || drama.equals("뷰티 인사이드")) {
            simpleResponse2
                    .setTextToSpeech("네, 뷰티 인사이드에 대해 알려드릴게요");
            String synopsis = "한 달에 일주일 타인의 얼굴로 살아가는 여자와 일 년 열두 달 타인의 얼굴을 알아보지 못하는 남자의 조금은 특별한 로맨스를 그린 드라마";
            String actors = "서현진, 이민기, 이다희";
            basicCard
                    .setTitle("뷰티 인사이드")
                    .setImage(new Image().setUrl("https://actions.o2o.kr/devsvr1/image/뷰티 인사이드.jpg")
                            .setAccessibilityText("뷰티 인사이드"))
                    .setFormattedText(synopsis + actors);
            netflixId = 81029990;
        } else if (selectedItem.equals("식샤") || drama.equals("식샤를 합시다2")) {
            simpleResponse2
                    .setTextToSpeech("네, 식샤를 합시다2에 대해 알려드릴게요");
            String synopsis = "구대영의 적수가 나타났다! 맛집 블로거 식샤님 '구대영'과 그를 원수로 기억하는 '1일1식 다이어트' 4년차 프리랜서 작가 '백수지', 초식남 공무원 '이상우'. 입맛 다른 '세종 빌라' 1인 가구들의 매콤하게 맛있는 드라마";
            String actors = "윤두준, 서현진, 권율";
            basicCard
                    .setTitle("식샤를 합시다2")
                    .setImage(new Image().setUrl("https://actions.o2o.kr/devsvr1/image/식샤를 합시다2.jpg")
                            .setAccessibilityText("식샤를 합시다2"))
                    .setFormattedText(synopsis + actors);
            netflixId = 80183878;
        } else if (selectedItem.equals("보보경심") || drama.equals("달의 연인 - 보보경심 려")) {
            simpleResponse2
                    .setTextToSpeech("네, 달의 연인 - 보보경심 려에 대해 알려드릴게요");
            String synopsis = "달그림자가 태양을 검게 물들인 날. 상처 입은 짐승 같은 사내, 4황자 '왕소'와 21세기 여인 '고하진'의 영혼이 미끄러져 들어간 고려 소녀 '해수'가 천 년의 시공간을 초월해 만난다.";
            String actors = "이준기, 이지은, 강하늘";
            basicCard
                    .setTitle("달의 연인 - 보보경심 려")
                    .setImage(new Image().setUrl("https://actions.o2o.kr/devsvr1/image/달의 연인 - 보보경심 려.jpg")
                            .setAccessibilityText("달의 연인  - 보보경심 려"))
                    .setFormattedText(synopsis + actors);
            netflixId = 80156759;
        } else if (selectedItem.equals("페르소나") || drama.equals("페르소나")) {
            simpleResponse2
                    .setTextToSpeech("네, 페르소나에 대해 알려드릴게요");
            String synopsis = "이경미, 임필성, 전고운, 김종관 4명의 감독이 페르소나 이지은을 각기 다른 시선으로 풀어낸 총 4편의 오리지널 시리즈";
            String actors = "이지은, 배두나, 박해수";
            basicCard
                    .setTitle("페르소나")
                    .setImage(new Image().setUrl("https://actions.o2o.kr/devsvr1/image/페르소나.jpg")
                            .setAccessibilityText("페르소나"))
                    .setFormattedText(synopsis + actors);
            netflixId = 81044884;
        } else if (selectedItem.equals("나저씨") || drama.equals("나의 아저씨")) {
            simpleResponse2
                    .setTextToSpeech("네, 나의 아저씨에 대해 알려드릴게요");
            String synopsis = "삶의 무게를 버티며 살아가는 아저씨 삼 형제와 거칠게 살아온 한 여성이 서로를 통해 삶을 치유하게 되는 이야기";
            String actors = "이선균, 이지은, 고두심";
            basicCard
                    .setTitle("나의 아저씨")
                    .setImage(new Image().setUrl("https://actions.o2o.kr/devsvr1/image/나의 아저씨.jpg")
                            .setAccessibilityText("나의 아저씨"))
                    .setFormattedText(synopsis + actors);
            netflixId = 81267691;
        } else if (selectedItem.equals("사이코") || drama.equals("사이코지만 괜찮아")) {
            simpleResponse2
                    .setTextToSpeech("네, 사이코지만 괜찮아에 대해 알려드릴게요");
            String synopsis = "버거운 삶의 무게로 사랑을 거부하는 정신 병동 보호사 강태와 태생적 결함으로 사랑을 모르는 동화 작가 문영이 서로의 상처를 보듬고 치유해가는 한 편의 판타지 동화 같은 사랑에 관한 조금 이상한 로맨틱 코미디";
            String actors = "김수현, 서예지, 오정세";
            basicCard
                    .setTitle("사이코지만 괜찮아")
                    .setImage(new Image().setUrl("https://actions.o2o.kr/devsvr1/image/사이코지만 괜찮아.jpg")
                            .setAccessibilityText("사이코지만 괜찮아"))
                    .setFormattedText(synopsis + actors);
            netflixId = 81243992;
        } else if (selectedItem.equals("멜체") || drama.equals("멜로가 체질")) {
            simpleResponse2
                    .setTextToSpeech("네, 멜로가 체질에 대해 알려드릴게요");
            String synopsis = "서른 살 여자 친구들의 고민, 연애, 일상을 그린 코믹 드라마";
            String actors = "천우희, 전여빈, 한지은";
            basicCard
                    .setTitle("멜로가 체질")
                    .setImage(new Image().setUrl("https://actions.o2o.kr/devsvr1/image/멜로가 체질.jpg")
                            .setAccessibilityText("멜로가 체질"))
                    .setFormattedText(synopsis + actors);
            netflixId = 81211284;
        } else if (selectedItem.equals("로별") || drama.equals("로맨스는 별책부록")) {
            simpleResponse2
                    .setTextToSpeech("네, 로맨스는 별책부록에 대해 알려드릴게요");
            String synopsis = "'책을 만들었는데, 로맨스가 따라왔다?' 책을 읽지 않는 세상에서 책을 만드는 사람들의 이야기를 담은 로맨틱 코미디 드라마";
            String actors = "이나영, 이종석, 정유진";
            basicCard
                    .setTitle("로맨스는 별책부록")
                    .setImage(new Image().setUrl("https://actions.o2o.kr/devsvr1/image/로맨스는 별책부록.jpg")
                            .setAccessibilityText("로맨스는 별책부록"))
                    .setFormattedText(synopsis + actors);
            netflixId = 81045349;
        } else if (selectedItem.equals("나홀로") || drama.equals("나 홀로 그대")) {
            simpleResponse2
                    .setTextToSpeech("네, 나 홀로 그대에 대해 알려드릴게요");
            String synopsis = "남모를 아픔을 숨기기 위해 스스로 외톨이가 된 소연과 다정하고 완벽한 인공지능 비서 홀로, 그와 얼굴은 같지만 성격은 정반대인 개발자 난도가 서로를 만나, 사랑할수록 외로워지는 불완전한 로맨스를 그리는 넷플릭스 오리지널 시리즈";
            String actors = "윤현민, 고성희, 최여진";
            basicCard
                    .setTitle("나 홀로 그대")
                    .setImage(new Image().setUrl("https://actions.o2o.kr/devsvr1/image/나 홀로 그대.jpg")
                            .setAccessibilityText("나 홀로 그대"))
                    .setFormattedText(synopsis + actors);
            netflixId = 81008021;
        } else if (selectedItem.equals("알함브라") || drama.equals("알함브라 궁전의 추억")) {
            simpleResponse2
                    .setTextToSpeech("네, 알함브라 궁전의 추억에 대해 알려드릴게요");
            String synopsis = "투자회사 대표인 남자주인공이 비즈니스로 스페인 그라나다에 갔다가 전직 기타리스트였던 여주인공이 운영하는 싸구려 호스텔에 묵으며 두 사람이 기묘한 사건에 휘말리며 펼쳐지는 이야기";
            String actors = "현빈, 박신혜, 박훈";
            basicCard
                    .setTitle("알함브라 궁전의 추억")
                    .setImage(new Image().setUrl("https://actions.o2o.kr/devsvr1/image/알함브라 궁전의 추억.jpg")
                            .setAccessibilityText("알함브라 궁전의 추억"))
                    .setFormattedText(synopsis + actors);
            netflixId = 81004280;
        } else if (selectedItem.equals("어비스") || drama.equals("어비스")) {
            simpleResponse2
                    .setTextToSpeech("네, 어비스에 대해 알려드릴게요");
            String synopsis = "\"영혼 소생 구슬\" 어비스를 통해 생전과 180도 다른 '반전 비주얼'로 부활한 두 남녀가 자신을 죽인 살인자를 쫓는 반전 비주얼 판타지";
            String actors = "박보영, 안효섭, 이성재";
            basicCard
                    .setTitle("어비스")
                    .setImage(new Image().setUrl("https://actions.o2o.kr/devsvr1/image/어비스.jpg")
                            .setAccessibilityText("어비스"))
                    .setFormattedText(synopsis + actors);
            netflixId = 81087762;
        } else if (selectedItem.equals("보이스") || drama.equals("보이스")) {
            simpleResponse2
                    .setTextToSpeech("네, 보이스에 대해 알려드릴게요");
            String synopsis = "범죄 현장의 골든타임을 사수하는 112 신고센터 대원들의 치열한 기록을 그린 소리 추격 스릴러 드라마";
            String actors = "이하나, 장혁, 이진욱";
            basicCard
                    .setTitle("보이스")
                    .setImage(new Image().setUrl("https://actions.o2o.kr/devsvr1/image/보이스.jpg")
                            .setAccessibilityText("보이스"))
                    .setFormattedText(synopsis + actors);
            netflixId = 80987095;
        } else if (selectedItem.equals("인간수업") || drama.equals("인간수업")) {
            simpleResponse2
                    .setTextToSpeech("네, 인간수업에 대해 알려드릴게요");
            String synopsis = "돈을 벌기 위해 죄책감없이 범죄의 길을 선택한 고등학생들이 그로 인해 돌이킬 수 없이 혹독한 대가를 치르는 과정을 그린 넷플릭스 오리지널 시리즈";
            String actors = "김동희, 정다빈, 박주현";
            basicCard
                    .setTitle("인간수업")
                    .setImage(new Image().setUrl("https://actions.o2o.kr/devsvr1/image/인간수업.jpg")
                            .setAccessibilityText("인간수업"))
                    .setFormattedText(synopsis + actors);
            netflixId = 80990668;
        } else if (selectedItem.equals("비숲") || drama.equals("비밀의 숲")) {
            simpleResponse2
                    .setTextToSpeech("네, 비밀의 숲에 대해 알려드릴게요");
            String synopsis = "감정을 느끼지 못하는 외톨이 검사 황시목이, 정의롭고 따뜻한 형사 한여진과 함께 검찰 스폰서 살인사건과 그 이면에 숨겨진 진실을 파헤치는 내부 비밀 추적극";
            String actors = "조승우, 배두나, 이준혁";
            basicCard
                    .setTitle("비밀의 숲")
                    .setImage(new Image().setUrl("https://actions.o2o.kr/devsvr1/image/비밀의 숲.jpg")
                            .setAccessibilityText("비밀의 숲"))
                    .setFormattedText(synopsis + actors);
            netflixId = 80187302;
        } else if (selectedItem.equals("시그널") || drama.equals("시그널")) {
            simpleResponse2
                    .setTextToSpeech("네, 시그널에 대해 알려드릴게요");
            String synopsis = "\"우리의 시간은 이어져있다.\" 과거로부터 걸려온 간절한 신호(무전)로 연결된 현재와 과거의 형사들이 오래된 미제 사건들을 다시 파헤친다!";
            String actors = "이제훈, 김혜수, 조진웅";
            basicCard
                    .setTitle("시그널")
                    .setImage(new Image().setUrl("https://actions.o2o.kr/devsvr1/image/시그널.jpg")
                            .setAccessibilityText("시그널"))
                    .setFormattedText(synopsis + actors);
            netflixId = 80987077;
        } else if (selectedItem.equals("킹덤") || drama.equals("킹덤")) {
            simpleResponse2
                    .setTextToSpeech("네, 킹덤에 대해 알려드릴게요");
            String synopsis = "죽은 자들이 살아나 생지옥이 된 위기의 조선, 왕권을 탐하는 조씨 일가의 탐욕과 누구도 믿을 수 없게 되어버린 왕세자 창의 피의 사투를 그린 미스터리 스릴러";
            String actors = "주지훈, 류승룡, 배두나";
            basicCard
                    .setTitle("킹덤")
                    .setImage(new Image().setUrl("https://actions.o2o.kr/devsvr1/image/킹덤.jpg")
                            .setAccessibilityText("킹덤"))
                    .setFormattedText(synopsis + actors);
            netflixId = 80180171;
        } else if (selectedItem.equals("미션") || drama.equals("미스터 션샤인")) {
            simpleResponse2
                    .setTextToSpeech("네, 미스터 션샤인에 대해 알려드릴게요");
            String synopsis = "신미양요(1871년) 때 군함에 승선해 미국에 떨어진 한 소년이 미국 군인 신분으로 자신을 버린 조국인 조선으로 돌아와 주둔하며 벌어지는 일을 그린 드라마";
            String actors = "이병헌, 김태리, 유연석";
            basicCard
                    .setTitle("미스터 션샤인")
                    .setImage(new Image().setUrl("https://actions.o2o.kr/devsvr1/image/미스터 션샤인.jpg")
                            .setAccessibilityText("미스터 션샤인"))
                    .setFormattedText(synopsis + actors);
            netflixId = 80991107;
        } else if (selectedItem.equals("더킹") || drama.equals("더 킹 - 영원의 군주")) {
            simpleResponse2
                    .setTextToSpeech("네, 더 킹 - 영원의 군주에 대해 알려드릴게요");
            String synopsis = "악마에 맞서 차원의 문(門)을 닫으려는 이과(理科)형 대한제국 황제와 누군가의 삶·사람·사랑을 지키려는 문과(文科)형 대한민국 형사의 공조를 통해 차원이 다른 로맨스를 그린 드라마";
            String actors = "이민호, 김고은, 우도환";
            basicCard
                    .setTitle("더 킹 - 영원의 군주")
                    .setImage(new Image().setUrl("https://actions.o2o.kr/devsvr1/image/더 킹 - 영원의 군주.jpg")
                            .setAccessibilityText("더 킹 - 영원의 군주"))
                    .setFormattedText(synopsis + actors);
            netflixId = 81260283;
        } else if (selectedItem.equals("도깨비") || drama.equals("도깨비")) {
            simpleResponse2
                    .setTextToSpeech("네, 도깨비에 대해 알려드릴게요");
            String synopsis = "불멸의 삶을 끝내기 위해 인간 신부가 필요한 도깨비, 그와 기묘한 동거를 시작한 기억상실증 저승사자. 그런 그들 앞에 '도깨비 신부'라 주장하는 '죽었어야 할 운명'의 소녀가 나타나며 벌어지는 신비로운 낭만 설화";
            String actors = "공유, 김고은, 이동욱";
            basicCard
                    .setTitle("도깨비")
                    .setImage(new Image().setUrl("https://actions.o2o.kr/devsvr1/image/도깨비.jpg")
                            .setAccessibilityText("도깨비"))
                    .setFormattedText(synopsis + actors);
            netflixId = 81012510;
        } else if (selectedItem.equals("디마프") || drama.equals("디어 마이 프렌즈")) {
            simpleResponse2
                    .setTextToSpeech("네, 디어 마이 프렌즈에 대해 알려드릴게요");
            String synopsis = "\"끝나지 않았다. 여전히 살아있다\"고 외치는 '황혼 청춘'들의 인생 찬가를 그린 드라마";
            String actors = "고현정, 김혜자, 고두심";
            basicCard
                    .setTitle("디어 마이 프렌즈")
                    .setImage(new Image().setUrl("https://actions.o2o.kr/devsvr1/image/디어 마이 프렌즈.jpg")
                            .setAccessibilityText("디어 마이 프렌즈"))
                    .setFormattedText(synopsis + actors);
            netflixId = 81267633;
        } else if (selectedItem.equals("그겨울") || drama.equals("그 겨울, 바람이 분다")) {
            simpleResponse2
                    .setTextToSpeech("네, 그 겨울, 바람이 분다에 대해 알려드릴게요");
            String synopsis = "유년시절 부모로부터 버려지고 첫사랑에 실패한 후 의미 없는 삶을 사는 남자와 부모의 이혼과 오빠와의 결별, 갑자기 찾아온 시각 장애로 외롭고 고단한 삶을 사는 여자가 만나 차갑고 외로웠던 그들의 삶에서 희망과 진정한 사랑의 의미를 찾아가는 이야기";
            String actors = "조인성, 송혜교, 김범";
            basicCard
                    .setTitle("그 겨울, 바람이 분다")
                    .setImage(new Image().setUrl("https://actions.o2o.kr/devsvr1/image/그 겨울, 바람이 분다.jpg")
                            .setAccessibilityText("그 겨울, 바람이 분다"))
                    .setFormattedText(synopsis + actors);
            netflixId = 70296735;
        }

//		simpleResponse2
//				.setTextToSpeech("네, " + drama + "에 대해 알려드릴게요");


        //해당 드라마의 포스터와 간단 줄거리 등을 보여줌
//		basicCard
//				.setTitle(drama)
//				.setImage(new Image().setUrl("https://actions.o2o.kr/devsvr1/image/" + drama + ".jpg")
//						.setAccessibilityText(drama))
//				.setFormattedText("소개 : " + synopsis + "\n배우 : " + actors);

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
    @ForIntent("watch")
    public ActionResponse watch(ActionRequest request) throws ExecutionException, InterruptedException {
        ResponseBuilder responseBuilder = getResponseBuilder(request);
        Map<String, Object> data = responseBuilder.getConversationData();

        data.clear();

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
        responseBuilder.add(linkOutSuggestion);

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
    @ForIntent("No Watch")
    public ActionResponse noWatch(ActionRequest request) throws ExecutionException, InterruptedException {
        ResponseBuilder responseBuilder = getResponseBuilder(request);
        Map<String, Object> data = responseBuilder.getConversationData();

        data.clear();

        SimpleResponse simpleResponse = new SimpleResponse();
        SimpleResponse simpleResponse2 = new SimpleResponse();
        BasicCard basicCard = new BasicCard();
        List<String> suggestions = new ArrayList<String>();

        simpleResponse
                .setDisplayText("다른 드라마를 선택해주세요.")
                .setTextToSpeech("다른 드라마를 선택해주세요");

        simpleResponse2
                .setTextToSpeech("장르, 배우 혹은 작가 중 하나를 선택해주세요.");

        basicCard
                .setImage(
                        new Image()
                                .setUrl("https://actions.o2o.kr/devsvr1/image/home.png")
                );

        suggestions.add("장르");
        suggestions.add("배우");
        suggestions.add("작가");

        responseBuilder.add(simpleResponse);
        responseBuilder.add(simpleResponse2);
        responseBuilder.add(basicCard);
        responseBuilder.addSuggestions(suggestions.toArray(new String[suggestions.size()]));

        return responseBuilder.build();
    }
}

