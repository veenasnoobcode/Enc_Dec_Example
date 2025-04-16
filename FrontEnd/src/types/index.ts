export interface EncryptionResponse {
  encryptedText: string;
  error?: string;
}

export interface DecryptionResponse {
  decryptedText: string;
  error?: string;
}
