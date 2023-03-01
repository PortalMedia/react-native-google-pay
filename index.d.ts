type EnvironmentType = number

export type AllowedCardNetworkType = 'AMEX' | 'DISCOVER' | 'JCB' | 'MASTERCARD' | 'VISA'

export type AllowedCardAuthMethodsType = 'PAN_ONLY' | 'CRYPTOGRAM_3DS'

export type tokenizationSpecificationType = 'PAYMENT_GATEWAY' | 'DIRECT'

export interface RequestDataType {
  cardPaymentMethod: {
    tokenizationSpecification: {
      type: tokenizationSpecificationType
      gateway: string
      gatewayMerchantId: string
    }
    allowedCardNetworks: AllowedCardNetworkType[]
    allowedCardAuthMethods: AllowedCardAuthMethodsType[]
    allowPrepaidCards: boolean
    allowCreditCards: boolean
    assuranceDetailsRequired: boolean
    billingAddressRequired: boolean
    billingAddressParameters: {
      format: string
      phoneNumberRequired: boolean
    }
  }
  transaction: {
    totalPrice: string
    totalPriceStatus: string
    currencyCode: string
  }
  merchantName: string,
  emailRequired: boolean,
  shippingAddressRequired: boolean,
}

export interface ResponsePaymentData {
  apiVersion: number,
  apiVersionMinor: number,
  email: string,
  paymentMethodData: PaymentMethodData,
  shippingAddress: Address
}

interface PaymentMethodData {
  type: string,
  description: string,
  info: PaymentMethodInfo,
  tokenizationData: TokenizationData
}

interface PaymentMethodInfo {
  cardNetwork: string,
  cardDetails: string,
  billingAddress: Address
  assuranceDetails: AssuranceDetails
}

interface TokenizationData {
  type: string,
  token: string,
}

interface AssuranceDetails {
  accountVerified: boolean,
  cardHolderAuthenticated: boolean,
}

export interface Address {
	address1: String;
	address2: String;
	address3: String;
	administrativeArea: String;
	countryCode: String;
	locality: String;
	name: String;
	phoneNumber: String;
	postalCode: String;
	sortingCode: String;
}

declare class GooglePay {
  static ENVIRONMENT_TEST: EnvironmentType
  static ENVIRONMENT_PRODUCTION: EnvironmentType
  static setEnvironment: (environment: EnvironmentType) => void
  static isReadyToPay: (
    allowedCardNetworks: AllowedCardNetworkType[],
    allowedCardAuthMethods: AllowedCardAuthMethodsType[]
  ) => Promise<boolean>
  static requestPayment: (requestData: RequestDataType) => Promise<string>
}

export { GooglePay }
