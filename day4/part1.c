#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>

#define MAX_WORDS 20 //max words per passphrase
#define MAX_WORDLEN 20 //max length of one word in passphrase
#define DELIM " \n"

bool is_valid(char *passphrase) {

    int word_count = 1; //assume at least one word
    char *strtok_state; 
    //allocate an array of up to 20 words
    char **words = (char**) calloc(20, sizeof(char*));
    char *current_word = NULL;

    //get first word
    words[0] = strtok_r(passphrase, DELIM, &strtok_state);
    if (!words[0]) {
        perror("No words in passphrase");
        exit(1);
    }

    //get the rest of words
    //foreach word, compare to the rest of the words in the list
    while((current_word = strtok_r(NULL, DELIM, &strtok_state))) {

        if (word_count > MAX_WORDS) {
            perror("TOO MANY WORDS");
            exit(1);
        }

        //compare word to all previous words
        for (int i = 0; i < MAX_WORDS; i++) {
            if (!words[i]) {
                //we've checked all the words so far
                break;
            }

            if (!strncmp(current_word, words[i], MAX_WORDLEN)) {
                //we found a duplicate!
                return false;
            } 
        }

        //add word to words array
        words[word_count] = current_word;
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