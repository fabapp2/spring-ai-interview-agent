
# Topic Schema Documentation

## Overview
The Topic object represents a unit of planning in the interview process. It links to sections of career data or conceptual narrative items.

---
## Fields

### id
- **Type:** `string`
- **Description:** MongoDB ObjectId (24 character hex string) uniquely identifying the topic.

### careerDataId
- **Type:** `string`
- **Description:** MongoDB ObjectId of the associated career data document.

### type
- **Type:** `string`
- **Description:** Defines the high-level category the topic addresses.
- **Allowed Values:** `basics`, `work`, `volunteer`, `education`, `projects`, `awards`, `certificates`, `publications`, `skills`, `languages`, `interests`, `references`, `gap`, `career_transition`, `freelance_period`, `timeline_arc`, `role`, `narrative`

### reference
- **Type:** `object`
- **Description:** Links the topic to specific items in the resume, if applicable.
- **Fields:**
  - `resumeItemId`: string (ID of a specific resume item)
  - `spans`: array of string (IDs for items spanned, e.g., freelance periods)
  - `startDate`: string (Optional ISO date string)
  - `endDate`: string (Optional ISO date string)
  - `resumeItemBeforeId`: string (Optional ID before a gap)
  - `resumeItemAfterId`: string (Optional ID after a gap)

### gapType
- **Type:** `string`
- **Description:** Defines if the gap is a time-based or timeline boundary type.
- **Allowed Values:** `time`, `timeline_boundary`

### reason
- **Type:** `string`
- **Description:** A free-text explanation why this topic was generated.

### reasonMeta
- **Type:** `object`
- **Description:** Optional metadata giving machine-readable context on why this topic was created.

### priorityScore
- **Type:** `integer`
- **Description:** A numeric score (0-100) indicating topic priority.

### priority
- **Type:** `string`
- **Description:** Human-readable categorization of priority.
- **Allowed Values:** `low`, `medium`, `high`

### createdAt
- **Type:** `string` (ISO 8601 date-time)
- **Description:** When the topic was created.

### updatedAt
- **Type:** `string` (ISO 8601 date-time)
- **Description:** When the topic was last modified.

---
## Example
```json
{
  "id": "662a2cf3b931226b6dcb3001",
  "careerDataId": "662a2cf3b931226b6dcb2001",
  "type": "work",
  "reference": {
    "resumeItemId": "w1234567"
  },
  "reason": "Detected missing achievements",
  "priorityScore": 85,
  "priority": "high",
  "createdAt": "2025-04-25T08:00:00Z",
  "updatedAt": "2025-04-25T08:30:00Z"
}
```
