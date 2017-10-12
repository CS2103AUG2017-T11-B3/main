package seedu.address.model.person;

import seedu.address.commons.util.StringUtil;

import java.util.Arrays;

import java.util.List;

import java.util.function.Predicate;

/**
 * Tests that a {@code ReadOnlyPerson}'s {@code Tags} matches any of the keywords given.
 */
public class TagContainsKeywordsPredicate implements Predicate<ReadOnlyPerson> {
    private final List<String> keywords;

    public TagContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(ReadOnlyPerson person) {
        String tag = Arrays.toString(person.getTags().toArray())
                .replaceAll("[\\[\\](),{}]","");
        return keywords.stream()
                        .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(tag, keyword));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof TagContainsKeywordsPredicate // instanceof handles nulls
                && this.keywords.equals(((TagContainsKeywordsPredicate) other).keywords)); // state check
    }

}
