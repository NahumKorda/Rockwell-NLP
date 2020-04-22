<h1>Rockwell</h1>

Rockwell is a natural language processing (NLP) platform designed specifically for term and information extraction.

Term extraction is a NLP task that extracts relevant terminology from a corpus of documents. Although term extraction can in some cases be successfully applied to a single document, the results are always better when a larger number of documents is processed. However, the term extraction assumes that the processed documents are thematically homogeneous, and that indeed they contain shared terminology.

The following term extraction methods are supported by Rockwell:

Single word extraction extract frequently used terms in a corpus. Inflected words create separate entries (for example, “extract”, “extracted”, “extracting” and “extracts” make separate entries). Some common words, such as the so-called stopwords (e.g., “the”, “I”, “should”, etc.),  contractions (e.g., “he’s”, “wasn’t”, “you’d”, etc.), digits and symbol characters (e.g., “$”, “%”, “&”)  can be ignored.

Lemma extraction works similar to the single term extraction, only various inflections of the same head word are grouped together, and represented by their canonical form (for example, “extract”, “extracted”, “extracting” and “extracts” are all grouped together and represented by “extract”). The common words that can be ignored are the same as in the single word extraction.

Phrase extraction extracts phrases consisting of two or more words. Phrases differ from the so-called n-grams in that they are always complete and meaningful, and not just any consecutive words. Extracted phrases could be restricted to include only noun, verb or adjective phrases. Some common phrases (e.g., “never ever”, “night and day”, “way to go”, etc.) can be ignored.

Information extraction is another, more complex NLP task. While term extraction surfaces whatever terminology is present in a corpus, information extraction identifies only specific information present in a text. If such information is not present, nothing is extracted.

The following information extraction methods are supported by Rockwell:

Classification identifies predefined categories in a text. Identification is carried out using the so-called Rockwell expressions that enable construction of complex identification rules and patterns. Sentiment analysis is also carried out using this method, and the sentiments are defined as categories that need to be identified.

Named entity recognition targets information identifiable by its formatting. Typically these are references to the real-world entities, such as people and organizations. However, it also includes recognition of dates, currencies, measures, etc.

Data extraction targets any data that can be identified by the context in which it occurs. The context can be defined by the document structure (for example, listing of parties in the header of an agreement), or by the wording introducing and/or concluding it (for example, “board of directors appointed [person] as the new chief executive officer”, where the phrase “board of directors appointed” introduces the information of interest - a person - and the phrase “as the new chief executive officer” concludes it). Data extraction is carried out using the so-called Rockwell frames.

For more information about Rockwell consult this document: https://docs.google.com/document/d/1CjDsEowbBLBOoJs1OrC4tV807-B14b1feAvtTepauHc/edit?usp=sharing.

For more information about Rockwell expressions consult this document: https://docs.google.com/document/d/1wMYCXAOm0cmJ4z5PHLRK5JyPsBY-p3vEYnupik-QT-A/edit?usp=sharing.

For more information about Rockwell frames consult this document: https://docs.google.com/document/d/16ehTwHFVetysFeySPHOQ8aue64FrN-F5dwVi2xKFVVc/edit?usp=sharing.

Rockwell is developed using OpenJDK 8, and provides Java API. The following document is the user manual containing instructions on how to operate Rockwell Java API.
