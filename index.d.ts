type EnvironmentType = number

export type AllowedCardNetworkType = 'AMEX' | 'DISCOVER' | 'JCB' | 'MASTERCARD' | 'VISA'

export type AllowedCardAuthMethodsType = 'PAN_ONLY' | 'CRYPTOGRAM_3DS'

export type TokenizationSpecificationType = 'PAYMENT_GATEWAY' | 'DIRECT'

export interface PaymentRequest {
  apiVersion: number
  apiVersionMinor: number
  environment: string
  allowedPaymentMethod: AllowedPaymentMethod
  transactionInfo: {
    totalPrice: string
    totalPriceStatus: string
    currencyCode: string
    totalPriceLabel: string | null
  }
  merchantInfo: {
    merchantName: string
  }
  emailRequired: boolean
  shippingAddressRequired: boolean
  shippingAddressParameters: {
    phoneNumberRequired: boolean
  } | null
}

interface AllowedPaymentMethod {
  type: string
  parameters: {
    allowedAuthMethods: AllowedCardAuthMethodsType[]
    allowedCardNetworks: AllowedCardNetworkType[]
    allowPrepaidCards: boolean
    allowCreditCards: boolean
    billingAddressRequired: boolean
    assuranceDetailsRequired: boolean
    billingAddressParameters: {
      format: string
      phoneNumberRequired: boolean
    }
  }
  tokenizationSpecification: {
    type: string
    parameters: {
      gateway: string
      gatewayMerchantId: string
    }
  }
}

export interface PaymentResponse {
  apiVersion: number,
  apiVersionMinor: number,
  email: string,
  paymentMethodData: PaymentMethodData,
  shippingAddress: Address | null
}

interface PaymentMethodData {
  type: string,
  description: string,
  info: {
    cardNetwork: string
    cardDetails: string
    billingAddress: Address
    assuranceDetails: {
      accountVerified: boolean
      cardHolderAuthenticated: boolean,
    }
  },
  tokenizationData: {
    type: string
    token: string
  }
}

interface Address {
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
  static requestPayment: (requestData: PaymentRequest) => Promise<string>
}

export { GooglePay }
