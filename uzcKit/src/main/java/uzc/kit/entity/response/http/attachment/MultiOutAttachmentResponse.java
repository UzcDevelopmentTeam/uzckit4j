package uzc.kit.entity.response.http.attachment;

import uzc.kit.entity.UzcAddress;
import uzc.kit.entity.UzcID;
import uzc.kit.entity.UzcValue;
import uzc.kit.entity.response.TransactionAttachment;
import uzc.kit.entity.response.attachment.MultiOutAttachment;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public final class MultiOutAttachmentResponse extends TransactionAttachmentResponse {

    private final MultiOutRecipient[] recipients;
    @SerializedName("version.MultiOutCreation")
    private final int version;

    public MultiOutAttachmentResponse(MultiOutRecipient[] recipients, int version) {
        this.recipients = recipients;
        this.version = version;
    }

    public MultiOutRecipient[] getRecipients() {
        return recipients;
    }

    public int getVersion() {
        return version;
    }

    @Override
    public TransactionAttachment toAttachment() {
        return new MultiOutAttachment(version, Arrays.stream(recipients).collect(Collectors.toMap(MultiOutRecipient::getRecipient, MultiOutRecipient::getAmount)));
    }

    public static class MultiOutRecipient {
        public static final JsonDeserializer<MultiOutRecipient> DESERIALIZER = (json, typeOfT, context) -> deserialize(json.getAsJsonArray());
        public static final JsonSerializer<MultiOutRecipient> SERIALIZER = (src, typeOfSrc, context) -> serialize(src);

        private static MultiOutRecipient deserialize(JsonArray source) {
            return new MultiOutRecipient(source.get(0).getAsString(), source.get(1).getAsString());
        }

        private static JsonArray serialize(MultiOutRecipient source) {
            JsonArray array = new JsonArray(2);
            array.add(source.getRecipient().getID());
            array.add(source.getAmount().toPlanck());
            return array;
        }

        private final String recipient;
        private final String amount;

        private MultiOutRecipient(String recipient, String amount) {
            this.recipient = recipient;
            this.amount = amount;
        }

        public UzcAddress getRecipient() {
            return UzcAddress.fromEither(recipient);
        }

        public UzcValue getAmount() {
            return UzcValue.fromPlanck(recipient);
        }
    }
}
