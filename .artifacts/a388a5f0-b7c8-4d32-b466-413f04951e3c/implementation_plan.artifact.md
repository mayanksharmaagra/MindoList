# Implementation Plan - Enhanced Bar Chart for Analytics

The goal is to implement a sophisticated "column bar" (Bar Chart) in the `AnalyticsScreen` using the provided reference code. This includes adding support for gradients, better styling, and potentially orientation/selection features if applicable.

## Proposed Changes

### [Component Name] Analytics UI

#### [MODIFY] [AnalyticsScreen.kt](file:///Users/mayanksharma/StudioProjects/MindoList/shared/src/commonMain/kotlin/com/jrprofessor/mindolist/screen/AnalyticsScreen.kt)

1.  **Define Gradients:** Add the color gradients (Ocean, Emerald, Sunset, Amethyst) as seen in the reference code.
2.  **Enhance Bar Chart Components:**
    *   Create a more advanced `BarChart` composable that mimics the reference code's API and visual style.
    *   Update `BarChartItem` to support gradients and rounded corners.
    *   Implement a simplified `BarDataSet` and `BarEntry` or adapt `ChartDataPoint` to include styling info.
3.  **Update `TasksCompletedChartCard`:** Replace the basic row of bars with the new `BarChart` component.
4.  **Integration:** Ensure the chart data from `AnalyticsViewModel` (Week, Month, Year) is correctly passed to the new components.

## Verification Plan

### Automated Tests
- Build the project to ensure no compilation errors.

### Manual Verification
- Deploy the app and navigate to the Analytics screen.
- Verify that the bar chart shows gradients and matches the reference style.
- Check if switching between Week, Month, and Year filters updates the chart correctly.
