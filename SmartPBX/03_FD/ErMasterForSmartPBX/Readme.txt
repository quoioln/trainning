=================================================
  ��ERMaster�p��Java�v���W�F�N�g�ɂ���
=================================================

���͂��߂�
���̃t�H���_�́ASmartPBX�J�X�R���J���ɂ�����
ERMaster�̃t�@�C��(DB�݌v��)���Ǘ����邽�߂�Eclipse�����v���W�F�N�g�ł��B


��.erm�t�@�C����ҏW������@
.erm�t�@�C����ҏW����ɂ́AEclipse���.erm�t�@�C�����J���K�v������܂��B
Eclipse�ɂ�ERMAster�̃v���O�C������������Ă���K�v������܂��B
�i���������@�́AERMaster�ɂ��Ă��Q�Ɓj


�ҏW����ꍇ�́A���̃t�H���_����Eclipse�ɃC���|�[�g���ĉ������B

�Ȃ��A�{�t�H���_��SVN�̃��|�W�g����ɂ���ꍇ�A�ȉ��̂����ꂩ�̕��@�ōs���ĉ������B
�E���̃t�H���_���w�肵�āAEclise�Ń`�F�b�N�A�E�g����B
  �iEclipse��SVN�������v���O�C���𓱓����邱�ƁB�j
�E���̃t�H���_��SVN����G�N�X�|�[�g���āAEclipse�ɃC���|�[�g����B


��.xls�t�@�C�����o�͂�����@
.erm���J������ʁi�r���[�j��ŉE�N���b�N���A[�G�N�X�|�[�g]��[Excel]��I�����ĉ������B

�e���v���[�g�t�@�C���͖{�v���W�F�N�g���ɂ���ȉ��𗘗p���ĉ������B
  template\SmartPBX_DB�݌v���e���v���[�g.xls

�o�͐�t�@�C���́A�{�v���W�F�N�g���Ǝw�肢�܂��B
SVN��̐݌v���Ƀ}�[�W���s���ĉ������B
�i��SVN���.xls�t�@�C�����㏑���ăR�~�b�g�͍s��Ȃ����ƁB�j



���Q�l�FERMaster�ɂ��āiAbout "ERMaster"�j
���C���X�g�[�����@�A�g�p���@�����J�ɉ������Ă��܂��B

�iEnglish�j
http://ermaster.sourceforge.net/
�i���{��j
http://ermaster.sourceforge.net/index_ja.html


��DDL�쐬���@

FD�I����A�ł����M�܂łɑ��₩�Ɏ��{����B
�쐬����͈̂ȉ��B
�E[CREATE_TABLE_Step(N-1).sql]
    �O��Step��DDL(�����̃J������CREATE TABLE�̂�)
�E[ALTER_TABLE_StepN.sql]
    �J������Step��DDL(�ǉ������e�[�u������CREATE TABLE�A�ǉ������J������ALTER TABLE)
    F�X�菇�Ŏg�p����B

�ȉ��̎菇��O���Step�ƍ����Step�Ŏ��{����B

0.�y�O��z
�EEclipse��SVN�֘A�̃v���O�C�����C���X�g�[���ς�
�EEclipse��ER Master���C���X�g�[���ς�
�EEclipse�Ɉȉ��̃v���W�F�N�g���`�F�b�N�A�E�g�ς�(�O��Step�������Step)

1. �u�@�\�݌v�� [�ʎ��R]�f�[�^�x�[�X�݌v��.erm�v��Eclipse��ŊJ��

2. erm�t�@�C���̕ҏW��ʁiER�}���o�Ă���j�ŁA�E�N���b�N���G�N�X�|�[�g��DDL ��I��

   �����͒l��
   �o�̓t�@�C���F�@�\�݌v�� [�ʎ��R]�f�[�^�x�[�X�݌v��.sql 
                 ���Ƃ肠����
   �t�@�C���G���R�[�h�FUTF-8
   ���s�R�[�h�FLF   �����f�t�H���g����ύX
   DROP�F�S�ă`�F�b�N
   CREATE�F�S�ă`�F�b�N
   �t�@�C���ۑ���ɊJ���F�`�F�b�N���O���Ă������ق����ǂ�
   
   ��L��ݒ肵[OK]�{�^��������

   �v���W�F�N�g�t�H���_�����Ɉȉ����o�͂���Ă���B
      {�v���W�F�N�g�t�H���_}\�@�\�݌v�� [�ʎ��R]�f�[�^�x�[�X�݌v��.sql

3. �o�͂��ꂽ�t�@�C�����R�s�[���t�@�C�����쐬����

[CREATE_TABLE_Step(N-1).sql]�̏ꍇ
�O���Step�ŏo�͂������̂ł���΂��̂܂܂ŗǂ��B
�i�����Step�ŏo�͂����ꍇ�A����̊J�����̃e�[�u���ƃJ�������폜����K�v������B�j
�������͑O��Step�ŏo�͂������̂��g�p���邱�ƁB


[ALTER_TABLE_StepN.sql]�̏ꍇ
��L�t�@�C����ҏW����B�ҏW���e�͈ȉ��̒ʂ�
  �E�����Step(�J������Step)�Œǉ������e�[�u����CREATE TABLE���݂̂ɂ���B
  �E�����Step��ALTER TABLE����CREATE�e�[�u�������Q�l�ɍ쐬����B

�쐬�����t�@�C�����ȉ��ɃR�s�[���R�~�b�g����B
(/svn/smartpbx-cuscon/)�z���� /trunk/{StepN}/30_DD
      StepN�E�E�E�� Step2.9

���[�J���ɏo�͂��ꂽ�ȉ��͍폜����B
�E�@�\�݌v�� [�ʎ��R]�f�[�^�x�[�X�݌v��.sql 
�E[CREATE_TABLE_Step(N-1).sql]
�E[ALTER_TABLE_StepN.sql]

�ȏ�
