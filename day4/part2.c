#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>

#define MAX_WORDS 20 //max words per passphrase
#define MAX_WORDLEN 20 //max length of one word in passphrase
#define DELIM " \n"

int *get_letter_count(char *word) {
    int *letter_count = (int *) calloc(26, sizeof(int));

    for (int i = 0; i < MAX_WORDLEN; i++) {
        if (!word[i]) {
            //end of word
            break;
        }

        int letter_idx = word[i] - 'a';
        letter_count[letter_idx]++;
    }

    return letter_count;
}

bool is_anagram(const int *w1, const int *w2) {

    for (int i = 0; i < 26; i++) {
        if (w1[i] != w2[i]) {
            return false;
        }
    }
    return true;
}

bool is_valid(char *passphrase) {

    int word_count = 1; //assume at least one word
    char *strtok_state; 

    //allocate an array of up to 20 letter-count arrays
    int **letter_counts = (int**) calloc(20, sizeof(int*));
    char *current_word = NULL;

    //get first word
    char *first_word = strtok_r(passphrase, DELIM, &strtok_state);
    if (!first_word) {
        perror("No words in passphrase");
        exit(1);
    }
    letter_counts[0] = get_letter_count(first_word);

    //get the rest of words
    //foreach word, compare to the rest of the words in the list
    while((current_word = strtok_r(NULL, DELIM, &strtok_state))) {

        if (word_count > MAX_WORDS) {
            perror("TOO MANY WORDS");
            exit(1);
        }

        //for each word, we allocate an array of 26 ints, to track letter frequency w/in the word
        //assume only lowercase letters a-z
        int *current_letter_count = get_letter_count(current_word);

        //compare word to all previous words
        for (int i = 0; i < MAX_WORDS; i++) {
            if (!letter_counts[i]) {
                //we've checked all the words so far
                break;
            }

            if (is_anagram(current_letter_count, letter_counts[i])) {
                //we found a duplicate!
                return false;
            } 
        }

        //add word to words array
        letter_counts[word_count] = current_letter_count;
        word_count++;
    }

    return true;
}

int main() {
    FILE *fp;
    int valid_passphrases = 0;
    char *passphrase = NULL;
    size_t len = 0;
    ssize_t nread;

    fp = fopen("passphrases.txt", "r");
    if (fp == NULL) {
        perror("failed to open file");
        exit(1);
    }

    while ((nread = getline(&passphrase, &len, fp)) != -1) {
        if (is_valid(passphrase)) {
            valid_passphrases++;
        }
    }

    fclose(fp);
    if (passphrase) {
        free(passphrase);
    }

    printf("Found %d valid passphrases\n", valid_passphrases);
    return 0;
}