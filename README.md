
# Schemes
## Functional Encryption

ABE for Circuits [GGHSW'13]  Sanjam Garg and Craig Gentry and Shai Halevi and Amit Sahai and Brent Waters 

In this work, we provide the first construction of Attribute-Based Encryption (ABE) for general circuits. Our construction is based on the existence of multilinear maps. We prove selective security of our scheme in the standard model under the natural multilinear generalization of the BDDH assumption. Our scheme achieves both Key-Policy and Ciphertext-Policy variants of ABE. Our scheme and its proof of security directly translate to the recent multilinear map framework of Garg, Gentry, and Halevi. This paper subsumes the manuscript of Sahai and Waters [SW12].
URL
http://eprint.iacr.org/2013/128.pdf 

ABE for Circuits [GGHVV'13]  Sanjam Garg and Craig Gentry and Shai Halevi and Amit Sahai and Brent Waters 

n this work, we provide the first construction of Attribute-Based Encryption (ABE) for general circuits. Our construction is based on the existence of multilinear maps. We prove selective security of our scheme in the standard model under the natural multilinear generalization of the BDDH assumption. Our scheme achieves both Key-Policy and Ciphertext-Policy variants of ABE. Our scheme and its proof of security directly translate to the recent multilinear map framework of Garg, Gentry, and Halevi. This paper subsumes the manuscript of Sahai and Waters [SW12]. 

ABE for Circuits [GGHVV'13]  Craig Gentry and Sergey Gorbunovy and Shai Haleviz and Vinod Vaikuntanathan and Dhinakaran Vinayagamurthy 

A fundamental question about (reusable) circuit garbling schemes is: how small can the garbled circuit be? Our main result is a reusable garbling scheme which produces garbled circuits that are the same size as the original circuit plus an additive poly(λ) bits, where λ is the security parameter. Save the additive poly(λ) factor, this is the best one could hope for. In contrast, all previous constructions of even single-use garbled circuits incurred a multiplicative poly(λ) blowup. Our techniques result in constructions of attribute-based and (single key secure) functional encryption schemes where the secret key of a circuit C consists of C itself, plus poly(λ) additional bits. All of these constructions are based on the subexponential hardness of the learning with errors problem. We also study the dual question of how short the garbled inputs can be, relative to the original input. We demonstrate a (different) reusable circuit garbling scheme, based on multilinear maps, where the size of the garbled input is the same as that of the original input, plus a poly(λ) factor. This improves on the result of Applebaum, Ishai, Kushilevitz and Waters (CRYPTO 2013) who showed such a result for single-use garbling. Similar to the above, this also results in attribute-based and (single key secure) functional encryption schemes where the size of the ciphertext encrypting an input x is the same as that of x, plus poly(λ) additional bits.
URL
http://eprint.iacr.org/2013/687.pdf 

Regular Languages [W'12] Brent Waters 


Abstract
We provide a functional encryption system that supports functionality for regular languages. In our system a secret key is associated with a Deterministic Finite Automata (DFA) M. A ciphertext CT encrypts a message m and is associated with an arbitrary length string w. A user is able to decrypt the ciphertext CT if and only if the DFA M associated with his private key accepts the string w. Compared with other known functional encryption systems, this is the first system where the functionality is capable of recognizing an unbounded language. For example, in (Key-Policy) Attribute-Based Encryption (ABE) a private key SK is associated with a single boolean formula φ which operates over a fixed number of boolean variables from the ciphertext. In contrast, in our system a DFA M will meaningfully operate over an arbitrary length input w. We propose a system that utilizes bilinear groups. Our solution is a “public index” system, where the message m is hidden, but the string w is not. We prove security in the selective model under a variant of the decision l-Bilinear Diffie-Hellman Exponent (BDHE) assumption that we call the decision l-Expanded BDHE problem.
URL
http://eprint.iacr.org/2012/384.pdf 

Unbounded HIBE [LW'11]


Unbounded HIBE and Attribute-Based Encryption
Authors
A. Lewko and B. Waters
Abstract
In this work, we present HIBE and ABE schemes which are ``unbounded" in the sense that the public parameters do not impose additional limitations on the functionality of the systems. In all previous constructions of HIBE in the standard model, a maximum hierarchy depth had to be fixed at setup. In all previous constructions of ABE in the standard model, either a small universe size or a bound on the size of attribute sets had to be fixed at setup. Our constructions avoid these limitations. We use a nested dual system encryption argument to prove full security for our HIBE scheme and selective security for our ABE scheme, both in the standard model and relying on static assumptions. Our ABE scheme supports LSSS matrices as access structures and also provides delegation capabilities to users.
URL
http://eprint.iacr.org/2010/197.pdf


Inner Product [LOSTW'10]

Fully Secure Functional Encryption: Attribute-Based Encryption and (Hierarchical) Inner Product Encryption
Authors
Allison Lewko and Tatsuaki Okamoto and Amit Sahai and Katsuyuki Takashima and Brent Waters
Abstract
In this paper, we present two fully secure functional encryption schemes. Our first result is a fully secure attribute-based encryption (ABE) scheme. Previous constructions of ABE were only proven to be selectively secure. We achieve full security by adapting the dual system encryption methodology recently introduced by Waters and previously leveraged to obtain fully secure IBE and HIBE systems. The primary challenge in applying dual system encryption to ABE is the richer structure of keys and ciphertexts. In an IBE or HIBE system, keys and ciphertexts are both associated with the same type of simple object: identities. In an ABE system, keys and ciphertexts are associated with more complex objects: attributes and access formulas. We use a novel information-theoretic argument to adapt the dual system encryption methodology to the more complicated structure of ABE systems. We construct our system in composite order bilinear groups, where the order is a product of three primes. We prove the security of our system from three static assumptions. Our ABE scheme supports arbitrary monotone access formulas. Our second result is a fully secure (attribute-hiding) predicate encryption (PE) scheme for inner-product predicates. As for ABE, previous constructions of such schemes were only proven to be selectively secure. Security is proven under a non-interactive assumption whose size does not depend on the number of queries. The scheme is comparably efficient to existing selectively secure schemes. We also present a fully secure hierarchical PE scheme under the same assumption. The key technique used to obtain these results is an elaborate combination of the dual system encryption methodology (adapted to the structure of inner product PE systems) and a new approach on bilinear pairings using the notion of dual pairing vector spaces (DPVS) proposed by Okamoto and Takashima.
URL
http://eprint.iacr.org/2010/110.pdf


Anonymous HIBE [DIP'10]


Fully Secure Anonymous HIBE and Secret-Key Anonymous IBE with Short Ciphertexts
Authors
A. De Caro and V. Iovino and G. Persiano
Abstract
Lewko and Waters [Eurocrypt 2010] presented a fully secure HIBE with short ciphertexts. In this paper we show how to modify their construction to achieve anonymity. We prove the security of our scheme under static (and generically secure) assumptions formulated in composite order bilinear groups. In addition, we present a fully secure Anonymous IBE in the secret-key setting. Secret-Key Anonymous IBE was implied by the work of [Shen-Shi-Waters - TCC 2009] which can be shown secure in the selective-id model. No previous fully secure construction of secret-key Anonymous IBE is known.
URL
http://eprint.iacr.org/2010/197.pdf


HVE [IP'08]


Hidden-Vector Encryption with Groups of Prime Order
Authors
V. Iovino and G. Persiano
Abstract
Predicate encryption schemes are encryption schemes in which each ciphertext Ct is associated with a binary attribute vector x = (x1, ..., xn) and keys K are associated with predicates. A key K can decrypt a ciphertext Ct if and only if the attribute vector of the cipher-text satisfies the predicate of the key. Predicate encryption schemes can be used to implement fine-grained access control on encrypted data and to perform search on encrypted data. Hidden vector encryption schemes [Boneh and Waters - TCC 2007] are encryption schemes in which each ciphertext Ct is associated with a binary vector x = (x1, ..., xn) and each key K is associated with binary vector y = (y1, ..., yn) with "don't care" entries (denoted with *). Key K can decrypt ciphertext Ct if and only if x and y agree for all i for which yi != *. Hidden vector encryption schemes are an important type of predicate encryption schemes as they can be used to construct more sophisticated predicate encryption schemes (supporting for example range and subset queries). We give a construction for hidden-vector encryption from standard complexity assumptions on bilinear groups of prime order. Previous constructions were in bilinear groups of composite order and thus resulted in less efficient schemes. Our construction is both payload-hiding and attribute-hiding meaning that also the privacy of the attribute vector, besides privacy of the cleartext, is guaranteed.
URL
http://eprint.iacr.org/2009/380.pdf

## Signature

SS [BLS'01]


Short signatures from the Weil pairing
Authors
D. Boneh, B. Lynn, and H. Shacham.
Abstract
We introduce a short signature scheme based on the Computational Diffie-Hellman assumption on certain elliptic and hyper-elliptic curves. For standard security parameters, the signature length is about half that of a DSA signature with a similar level of security. Our short signature scheme is designed for systems where signatures are typed in by a human or are sent over a low-bandwidth channel. We survey a number of properties of our signature scheme such as signature aggregation and batch verification.
URL
http://crypto.stanford.edu/~dabo/abstracts/weilsigs.html

IBS [PS'06]


Efficient Identity-based Signatures Secure in the Standard Model
Authors
Kenneth G. Paterson and Jacob C. N. Schuldt
Abstract
The only known construction of identity-based signatures that can be proven secure in the standard model is based on the approach of attaching certificates to non-identity-based signatures. This folklore construction method leads to schemes that are somewhat inefficient and leaves open the problem of finding more efficient direct constructions. We present the first such construction. Our scheme is obtained from a modification of Waters’ recently proposed identity-based encryption scheme. It is computationally efficient and the signatures are short. The scheme’s security is proven in the standard model and rests on the hardness of the computational Diffie-Hellman problem in groups equipped with a pairing.
URL
http://eprint.iacr.org/2009/380.pdf

