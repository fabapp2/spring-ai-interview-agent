
# Conversation Item Schema Documentation

## Overview
The Conversation Item object represents a single utterance in the dialogue, tied to a specific thread. Each entry records who said what and when.

---
## Fields

### id
- **Type:** `string`
- **Description:** MongoDB ObjectId (24 character hex string) uniquely identifying the conversation item.

### threadId
- **Type:** `string`
- **Description:** MongoDB ObjectId linking this message to its parent thread.

### timestamp
- **Type:** `string`
- **Format:** ISO 8601 date-time
- **Description:** Timestamp when the message occurred.

### role
- **Type:** `string`
- **Description:** Role of the sender in the conversation.
- **Allowed Values:** `user`, `assistant`, `system`

### content
- **Type:** `string`
- **Description:** The text content of the message.

### type
- **Type:** `string`
- **Description:** Optional classification of the message.
- **Allowed Values:** `question`, `answer`, `command`, `note`

---
## Example
```json
{
  "id": "662a2cf3b931226b6dcb3055",
  "threadId": "662a2cf3b931226b6dcb3023",
  "timestamp": "2025-04-25T10:10:00Z",
  "role": "assistant",
  "content": "Can you describe the key challenges you faced in your last role?",
  "type": "question"
}
```
