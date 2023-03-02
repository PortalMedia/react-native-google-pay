package com.busfor;

import android.app.Activity;

import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.wallet.Wallet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.facebook.react.bridge.ReadableMap;

public class PaymentsUtil {

  private PaymentsUtil() {}

  public static PaymentsClient createPaymentsClient(int environment, Activity activity) {
    Wallet.WalletOptions walletOptions = new Wallet.WalletOptions.Builder().setEnvironment(environment).build();
    return Wallet.getPaymentsClient(activity, walletOptions);
  }

  public static JSONObject getIsReadyToPayRequest(ArrayList allowedCardNetworks, ArrayList allowedCardAuthMethods) {
    try {
      JSONObject isReadyToPayRequest = new JSONObject();

      // API Version
      isReadyToPayRequest.put("apiVersion", 2);
      isReadyToPayRequest.put("apiVersionMinor", 0);

      // Allowed Payment Methods
      JSONArray allowedPaymentMethods = new JSONArray();
      JSONObject allowedPaymentMethod = new JSONObject();
      allowedPaymentMethod.put("type", "CARD");

      // Allowed Payment Methods: Parameters
      JSONObject parameters = new JSONObject();
      parameters.put("allowedAuthMethods", new JSONArray(allowedCardAuthMethods));
      parameters.put("allowedCardNetworks", new JSONArray(allowedCardNetworks));
      allowedPaymentMethod.put("parameters", parameters);
      allowedPaymentMethods.put(allowedPaymentMethod);

      isReadyToPayRequest.put("allowedPaymentMethods", allowedPaymentMethods);
      return isReadyToPayRequest;
    } catch (JSONException e) {
      return null;
    }
  }

  private static JSONObject getTransactionInfo(ReadableMap transactionInfoData) throws JSONException {
    JSONObject transactionInfo = new JSONObject();
    transactionInfo.put("totalPrice", transactionInfoData.getString("totalPrice"));
    transactionInfo.put("totalPriceStatus", transactionInfoData.getString("totalPriceStatus"));
    transactionInfo.put("currencyCode", transactionInfoData.getString("currencyCode"));
    return transactionInfo;
  }

  private static JSONObject getMerchantInfo(ReadableMap merchantInfoData) throws JSONException {
    JSONObject merchantInfo = new JSONObject();
    merchantInfo.put("merchantName", merchantInfoData.getString("merchantName"));
    return merchantInfo;
  }

  private static JSONObject getTokenizationSpecification(ReadableMap tokenizationSpecData) throws JSONException {
    JSONObject tokenizationSpec = new JSONObject();
    tokenizationSpec.put("type", tokenizationSpecData.getString("type"));

    JSONObject parameters = new JSONObject();
    ReadableMap parametersData = tokenizationSpecData.getMap("parameters");
    parameters.put("gateway", parametersData.getString("gateway"));
    parameters.put("gatewayMerchantId", parametersData.getString("gatewayMerchantId"));

    tokenizationSpec.put("parameters", parameters);
    return tokenizationSpec;
  }

  private static JSONObject getCardPaymentMethodParameters(ReadableMap paramsData) throws JSONException {
    JSONObject parameters = new JSONObject();

    // Allowed Auth Methods and Card Networks
    ArrayList allowedAuthMethods = paramsData.getArray("allowedAuthMethods").toArrayList();
    ArrayList allowedCardNetworks = paramsData.getArray("allowedCardNetworks").toArrayList();
    parameters.put("allowedAuthMethods", new JSONArray(allowedAuthMethods));
    parameters.put("allowedCardNetworks", new JSONArray(allowedCardNetworks));

    // Billing Address Required
    if (paramsData.hasKey("billingAddressRequired")) {
      boolean billingAddressRequired = paramsData.getBoolean("billingAddressRequired");
      parameters.put("billingAddressRequired", billingAddressRequired);

      // Billing Address Parameters
      if (billingAddressRequired && paramsData.hasKey("billingAddressParameters")) {
        JSONObject billingAddressParameters = new JSONObject();
        ReadableMap billingAddressParametersData = paramsData.getMap("billingAddressParameters");

        // Format
        if (billingAddressParametersData.hasKey("format")) {
          billingAddressParameters.put("format", billingAddressParametersData.getString("format"));
        } else {
          // default to FULL if not supplied
          billingAddressParameters.put("format", "FULL");
        }

        // Phone Number Required
        if (billingAddressParametersData.hasKey("phoneNumberRequired")) {
          billingAddressParameters.put("phoneNumberRequired", billingAddressParametersData.getBoolean("phoneNumberRequired"));
        } else {
          // default to true if not supplied
          billingAddressParameters.put("phoneNumberRequired", true);
        }

        parameters.put("billingAddressParameters", billingAddressParameters);
      }
    }

    // Assurance Details Required
    if (paramsData.hasKey("assuranceDetailsRequired")) {
      parameters.put("assuranceDetailsRequired", paramsData.getBoolean("assuranceDetailsRequired"));
    } else {
      // default to false if not supplied
      parameters.put("assuranceDetailsRequired", false);
    }

    // Allow Credit Cards
    if (paramsData.hasKey("allowCreditCards")) {
      parameters.put("allowCreditCards", paramsData.getBoolean("allowCreditCards"));
    } else {
      // default to true if not supplied
      parameters.put("allowCreditCards", true);
    }

    // Allow Prepaid Cards
    if (paramsData.hasKey("allowPrepaidCards")) {
      parameters.put("allowPrepaidCards", paramsData.getBoolean("allowPrepaidCards"));
    } else {
      // default to true if not supplied
      parameters.put("allowPrepaidCards", true);
    }

    return parameters;
  }

  private static JSONObject getCardPaymentMethod(ReadableMap allowedPaymentMethodData) throws JSONException {
    JSONObject cardPaymentMethod = new JSONObject();

    // Type
    if (allowedPaymentMethodData.hasKey("type")) {
      cardPaymentMethod.put("type", allowedPaymentMethodData.getString("type"));
    }

    // Parameters (CARD)
    cardPaymentMethod.put("parameters", getCardPaymentMethodParameters(allowedPaymentMethodData.getMap("parameters")));

    // Tokenization Specification
    cardPaymentMethod.put("tokenizationSpecification", getTokenizationSpecification(allowedPaymentMethodData.getMap("tokenizationSpecification")));

    return cardPaymentMethod;
  }

  public static JSONObject getPaymentDataRequest(ReadableMap requestData) {
    try {
      JSONObject paymentRequest = new JSONObject();

      // API Version
      if (requestData.hasKey("apiVersion") && requestData.hasKey("apiVersionMinor")) {
        paymentRequest.put("apiVersion", requestData.getInt("apiVersion"));
        paymentRequest.put("apiVersionMinor", requestData.getInt("apiVersionMinor"));
      } else {
        // set default values if not supplied
        paymentRequest.put("apiVersion", 2).put("apiVersionMinor", 0);
      }

      // Allowed Payment Methods
      paymentRequest.put("allowedPaymentMethods", new JSONArray().put(PaymentsUtil.getCardPaymentMethod(requestData.getMap("allowedPaymentMethod"))));

      // Transaction Info
      paymentRequest.put("transactionInfo", PaymentsUtil.getTransactionInfo(requestData.getMap("transactionInfo")));

      // Merchant Info
      paymentRequest.put("merchantInfo", PaymentsUtil.getMerchantInfo(requestData.getMap("merchantInfo")));

      // Email Required
      if (requestData.hasKey("emailRequired")) {
        paymentRequest.put("emailRequired", requestData.getBoolean("emailRequired"));
      } else {
        // default to true if not supplied
        paymentRequest.put("emailRequired", true);
      }

      // Shipping Address Required
      if (requestData.hasKey("shippingAddressRequired")) {
        boolean shippingAddressRequired = requestData.getBoolean("shippingAddressRequired");
        paymentRequest.put("shippingAddressRequired", shippingAddressRequired);
        if (shippingAddressRequired && requestData.hasKey("shippingAddressParameters")) {
          ReadableMap shipAddressParamData = requestData.getMap("shippingAddressParameters");
          JSONObject shipAddressParams = new JSONObject();
          if (shipAddressParamData.hasKey("phoneNumberRequired")) {
            shipAddressParams.put("phoneNumberRequired", shipAddressParamData.getBoolean("phoneNumberRequired"));
          } else {
            // default to true if not supplied
            shipAddressParams.put("phoneNumberRequired", true);
          }
          paymentRequest.put("shippingAddressParameters", shipAddressParams);
        }
      } else {
        // default to false if not supplied
        paymentRequest.put("shippingAddressRequired", false);
      }

      return paymentRequest;
    } catch (JSONException e) {
      return null;
    }
  }
}
