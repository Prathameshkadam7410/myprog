import dayjs from 'dayjs';

export interface ISubscriptionDetails {
  id?: number;
  subscriptionName?: string;
  subscriptionAmount?: number;
  taxAmount?: number;
  totalAmount?: number;
  subscriptionStartDate?: dayjs.Dayjs;
  subscriptionExpiryDate?: dayjs.Dayjs;
  additionalComments?: string;
  category?: string;
  notificationBeforeExpiry?: number;
  notificationMuteFlag?: boolean;
  notificationTo?: string;
  notificationCc?: string | null;
  notificationBcc?: string | null;
}

export const defaultValue: Readonly<ISubscriptionDetails> = {
  notificationMuteFlag: false,
};
