# 📚 Topic Object Documentation

This section defines the `topic` object used within the interview plan schema.

---

## 🧩 Overview

Each topic represents an **interviewable unit** tied to either:
- A specific item in the resume (e.g., a job or project),
- A narrative concept (e.g., a gap or career transition),
- A thematic span across resume items.

---

## 🧠 Field Reference

### `id` (string, required)
A stable, unique identifier for the topic — must be a NanoID.

```json
"id": "z8F2xV1A"
```

---

### `type` (string, required)
Specifies the category of the topic.

#### Resume-aligned values:
- `work`, `volunteer`, `education`, `projects`, `awards`, `certificates`, `publications`, `skills`, `languages`, `interests`, `references`

#### Narrative/system-generated values (with examples below):
- `gap`, `career_transition`, `freelance_period`, `timeline_arc`, `role`, `narrative`

##### Example:
```json
"type": "gap"
```

---

### `reference` (object, optional)
Links the topic to resume items, spans, or timelines.

#### Fields:
- `resumeItemId` (string): NanoID of a resume entry
- `spans` (array of strings): NanoIDs of multiple related resume entries
- `startDate` / `endDate` (string, YYYY or YYYY-MM): Period this topic applies to
- `resumeItemBeforeId`, `resumeItemAfterId` (string): Context for gaps

##### Example:
```json
"reference": {
  "startDate": "2022-05",
  "endDate": "2022-12",
  "resumeItemBeforeId": "q1w2e3r4",
  "resumeItemAfterId": "t5y6u7i8"
}
```

---

### `gapType` (string, conditional)
Only used when `type` is `gap`.

- Values: `"time"` | `"timeline_boundary"`

##### Example:
```json
"gapType": "timeline_boundary"
```

---

### `reason` (string, optional)
Human-readable explanation of why the topic exists.

##### Example:
```json
"reason": "Detected a 7-month gap between roles."
```

---

### `reasonMeta` (object, optional)
Machine-readable metadata explaining topic creation.

#### Fields (examples):
- `detectedBy`: method or subsystem
- `gapDurationInMonths`: e.g., `7`
- `patternType`: for arcs
- `confidenceScore`: float between 0–1
- `timestamp`: ISO date

##### Example:
```json
"reasonMeta": {
  "detectedBy": "timeline_gap_analysis",
  "gapDurationInMonths": 8,
  "confidenceScore": 0.85,
  "timestamp": "2025-04-24T15:30:00Z"
}
```

---

### `priorityScore` (integer, optional)
Score from `0` to `100` representing importance.

##### Example:
```json
"priorityScore": 92
```

---

### `priority` (string, optional, derived)
Derived from `priorityScore`:
- `"low"` if 0–39
- `"medium"` if 40–69
- `"high"` if 70–100

##### Example:
```json
"priority": "high"
```

---

### `createdAt` / `updatedAt` (string, optional)
Timestamps in ISO 8601 format.

##### Example:
```json
"createdAt": "2025-04-24T15:42:00Z"
"updatedAt": "2025-04-25T09:10:30Z"
```

---

# 📘 Example Topic (Narrative Type)

```json
{
  "id": "z8F2xV1A",
  "type": "gap",
  "reference": {
    "startDate": "2022-05",
    "endDate": "2022-12",
    "resumeItemBeforeId": "abc123xy",
    "resumeItemAfterId": "def456gh"
  },
  "gapType": "time",
  "reason": "Detected a 7-month gap between known roles",
  "reasonMeta": {
    "detectedBy": "timeline_gap_analysis",
    "gapDurationInMonths": 7,
    "timestamp": "2025-04-25T08:00:00Z"
  },
  "priorityScore": 95,
  "createdAt": "2025-04-25T08:01:00Z",
  "updatedAt": "2025-04-25T08:15:00Z"
}
```
