package model.resource;

// ResourceStatus centralizes valid item states, which supports SRP and avoids magic strings.
public enum ResourceStatus {
    AVAILABLE,
    SOLD,
    BORROWED,
    RESERVED
}
