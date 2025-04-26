
# Thread Schema Documentation

## Overview
The Thread object represents a focused unit of interaction within a topic. Each thread describes a small dialogue goal and links back to its parent topic.

---
## Fields

### id
- **Type:** `string`
- **Description:** MongoDB ObjectId (24 character hex string) uniquely identifying the thread.

### topicId
- **Type:** `string`
- **Description:** MongoDB ObjectId linking the thread to its parent topic.

### type
- **Type:** `string`
- **Description:** Defines the inquiry focus of the thread.
- **Allowed Values:** `core_details`, `achievements`, `responsibilities`, `skills_used`, `team_context`, `challenges`, `transition`, `impact`, `learning`, `collaboration`, `technical_depth`, `project_specifics`, `certification_details`, `publication_impact`, `skill_application`, `language_usage`, `interest_relevance`

### focus
- **Type:** `string`
- **Description:** Optional freeform refinement text about what this thread focuses on.

### status
- **Type:** `string`
- **Description:** Current state of the thread lifecycle.
- **Allowed Values:** `pending`, `in_progress`, `on_hold`, `completed`, `skipped`

### duration
- **Type:** `integer`
- **Description:** Intended estimated duration for handling this thread (in seconds).

### actualDuration
- **Type:** `integer`
- **Description:** Real recorded time spent on the thread (in seconds).

### relatedThreads
- **Type:** `array of string`
- **Description:** Optional list of IDs of related threads for contextual enrichment.

### contextObject
- **Type:** `object`
- **Description:** Optional structured object providing additional prompt context.

### contextAction
- **Type:** `string`
- **Description:** Suggests the preferred agent interaction style.
- **Allowed Values:** `ask`, `confirm`, `validate`, `summarize`, `generate`, `reflect`

### contextGoal
- **Type:** `string`
- **Description:** Describes what the thread should achieve as an end goal.
- **Allowed Values:** `extract_responsibilities`, `capture_achievements`, `understand_team_context`, `validate_timeframes`, `enrich_skill_application`, `describe_challenges`, `identify_learning_outcomes`, `assess_language_usage`, `map_interest_relevance`

### createdAt
- **Type:** `string` (ISO 8601 date-time)
- **Description:** Timestamp when the thread was created.

### updatedAt
- **Type:** `string` (ISO 8601 date-time)
- **Description:** Timestamp when the thread was last modified.

---
## Example
```json
{
  "id": "662a2cf3b931226b6dcb3023",
  "topicId": "662a2cf3b931226b6dcb3001",
  "type": "responsibilities",
  "focus": "Gather core backend development responsibilities",
  "status": "pending",
  "duration": 300,
  "contextAction": "ask",
  "contextGoal": "extract_responsibilities",
  "createdAt": "2025-04-25T09:00:00Z",
  "updatedAt": "2025-04-25T09:15:00Z"
}
```
