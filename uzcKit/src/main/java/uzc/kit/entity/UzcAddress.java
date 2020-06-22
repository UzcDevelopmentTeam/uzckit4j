package uzc.kit.entity;

import uzc.kit.crypto.UzcCrypto;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;

import java.util.Objects;

@SuppressWarnings("WeakerAccess")
public final class UzcAddress {

    /**
     * Stored without "UZC-" prefix.
     */
    private final String address;
    private final UzcID numericID;

    private UzcAddress(UzcID uzcID) {
        this.numericID = uzcID;
        this.address = UzcCrypto.getInstance().rsEncode(numericID);
    }

    /**
     * @param uzcID The numeric id that represents this Uzc Address
     * @return A UzcAddress object that represents the specified numericId
     * @throws NumberFormatException if the numericId is not a valid number
     * @throws IllegalArgumentException if the numericId is outside the range of accepted numbers (less than 0 or greater than / equal to 2^64)
     */
    public static UzcAddress fromId(UzcID uzcID) {
        return new UzcAddress(uzcID);
    }

    /**
     * @param signedLongId The numeric id that represents this Uzc Address, as a signed long
     * @return A UzcAddress object that represents the specified numericId
     * @throws NumberFormatException if the numericId is not a valid number
     * @throws IllegalArgumentException if the numericId is outside the range of accepted numbers (less than 0 or greater than / equal to 2^64)
     */
    public static UzcAddress fromId(long signedLongId) {
        return new UzcAddress(UzcID.fromLong(signedLongId));
    }

    /**
     * @param unsignedLongId The numeric id that represents this Uzc Address
     * @return A UzcAddress object that represents the specified numericId
     * @throws NumberFormatException if the numericId is not a valid number
     * @throws IllegalArgumentException if the numericId is outside the range of accepted numbers (less than 0 or greater than / equal to 2^64)
     */
    public static UzcAddress fromId(String unsignedLongId) {
        return new UzcAddress(UzcID.fromLong(unsignedLongId));
    }

    public static UzcAddress fromRs(String RS) throws IllegalArgumentException {
        if (RS.startsWith("UZC-")) {
            RS = RS.substring(4);
        }
        return new UzcAddress(UzcCrypto.getInstance().rsDecode(RS));
    }

    /**
     * Try to parse an input as either a numeric ID or an RS address.
     *
     * @param input the numeric ID or RS address of the Uzc address
     * @return a UzcAddress if one could be parsed from the input, null otherwise
     */
    public static UzcAddress fromEither(String input) {
        if (input == null) return null;
        try {
            return UzcAddress.fromId(UzcID.fromLong(input));
        } catch (IllegalArgumentException e1) {
            try {
                return UzcAddress.fromRs(input);
            } catch (IllegalArgumentException e2) {
                return null;
            }
        }
    }

    /**
     * @return The UzcID of this address
     */
    public UzcID getUzcID() {
        return numericID;
    }

    /**
     * @return The unsigned long numeric ID this UzcAddress points to
     */
    public String getID() {
        return numericID.getID();
    }

    /**
     * @return The signed long numeric ID this UzcAddress points to
     */
    public long getSignedLongId() {
        return numericID.getSignedLongId();
    }

    /**
     * @return The ReedSolomon encoded address, without the "UZC-" prefix
     */
    public String getRawAddress() {
        return address;
    }

    /**
     * @return The ReedSolomon encoded address, with the "UZC-" prefix
     */
    public String getFullAddress() {
        if (address == null || address.length() == 0) {
            return "";
        } else {
            return "UZC-" + address;
        }
    }

    @Override
    public String toString() {
        return getFullAddress();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof UzcAddress && Objects.equals(numericID, ((UzcAddress) obj).numericID);
    }

    @Override
    public int hashCode() {
        return numericID.hashCode();
    }
}
