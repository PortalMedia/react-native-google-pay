type EnvironmentType = number

export type AllowedCardNetworkType = 'AMEX' | 'DISCOVER' | 'JCB' | 'MASTERCARD' | 'VISA'

export type AllowedCardAuthMethodsType = 'PAN_ONLY' | 'CRYPTOGRAM_3DS'

export type tokenizationSpecificationType = 'PAYMENT_GATEWAY' | 'DIRECT'

export interface RequestDataType {
  cardPaymentMethod: {
    tokenizationSpecification: {
      type: tokenizationSpecificationType
      /** only with type: PAYMENT_GATEWAY */
      gateway?: string
      /** only with type: PAYMENT_GATEWAY */
      gatewayMerchantId?: string
      /** only with gateway: stripe */
      stripe?: {
        publishableKey: string
        version: string
      }
      /** only with type: DIRECT */
      publicKey?: string
    }
    allowedCardNetworks: AllowedCardNetworkType[]
    allowedCardAuthMethods: AllowedCardAuthMethodsType[]
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
  shippingAddress: ShippingAddress
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
}

interface TokenizationData {
  type: string,
  token: string,
}

interface ShippingAddress {
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
