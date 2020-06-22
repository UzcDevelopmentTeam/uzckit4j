package uzc.kit.entity.response;

import uzc.kit.entity.UzcValue;
import uzc.kit.entity.response.http.SuggestFeeResponse;
import uzc.kit.service.impl.grpc.BrsApi;

public class FeeSuggestion {
    private final UzcValue cheapFee;
    private final UzcValue standardFee;
    private final UzcValue priorityFee;

    public FeeSuggestion(UzcValue cheapFee, UzcValue standardFee, UzcValue priorityFee) {
        this.cheapFee = cheapFee;
        this.standardFee = standardFee;
        this.priorityFee = priorityFee;
    }

    public FeeSuggestion(SuggestFeeResponse suggestFeeResponse) {
        this.cheapFee = UzcValue.fromPlanck(suggestFeeResponse.getCheap());
        this.standardFee = UzcValue.fromPlanck(suggestFeeResponse.getStandard());
        this.priorityFee = UzcValue.fromPlanck(suggestFeeResponse.getPriority());
    }

    public FeeSuggestion(BrsApi.FeeSuggestion feeSuggestion) {
        this.cheapFee = UzcValue.fromPlanck(feeSuggestion.getCheap());
        this.standardFee = UzcValue.fromPlanck(feeSuggestion.getStandard());
        this.priorityFee = UzcValue.fromPlanck(feeSuggestion.getPriority());
    }

    public UzcValue getCheapFee() {
        return cheapFee;
    }

    public UzcValue getStandardFee() {
        return standardFee;
    }

    public UzcValue getPriorityFee() {
        return priorityFee;
    }
}
