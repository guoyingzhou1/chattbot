import com.hankcs.hanlp.corpus.io.IOUtil
import com.hankcs.hanlp.mining.word2vec.DocVectorModel
import com.hankcs.hanlp.mining.word2vec.Word2VecTrainer
import com.hankcs.hanlp.mining.word2vec.WordVectorModel
import java.io.IOException
import java.util

object diWord {

  private val TRAIN_FILE_NAME = "F:\\test\\HanLPdatas\\data\\test\\搜狗文本分类语料库已分词.txt"
  private val MODEL_FILE_NAME = "F:\\test\\HanLPdatas\\data\\test\\word2vecTest.txt"

  @throws[IOException]
  def main(args: Array[String]): Unit = {
    val wordVectorModel = trainOrLoadModel
    //        System.out.println(wordVectorModel.nearest("高兴"));
    //        System.out.println("-------++++++++++++++++++++++");
    //        printNearest("飞机", wordVectorModel);
    //        printNearest("美丽", wordVectorModel);
    //        printNearest("购买", wordVectorModel);
    // 文档向量
    val docVectorModel = new DocVectorModel(wordVectorModel)
    val documents = Array[String]("我今天很不开心", "农民在江苏种水稻", "我要坐火车", "世界锦标赛胜出", "山东足球失败")
    var i = 0
    while (i < documents.length) {
      docVectorModel.addDocument(i, documents(i))
      i += 1
    }
    //得到最相似的句子
    val sk = docVectorModel.nearest("粮食好贵").get(0).getKey
    println(documents(sk))
  }

  def printNearest(word: String, model: WordVectorModel): Unit = {
    printf("\n                   Word     Cosine\n------------------------------------------------------------------------\n")
    import scala.collection.JavaConversions._
    for (entry <- model.nearest(word)) {
      printf("%50s\t\t%f\n", entry.getKey, entry.getValue)
    }
  }

  def printNearestDocument(document: String, documents: Array[String], model: DocVectorModel): Unit = {
    import scala.collection.JavaConversions._
    for (entry <- model.nearest(document)) {
      //            System.out.printf("%50s\t\t%f\n", documents[entry.getKey()], entry.getValue());
    }
  }

  private def printHeader(query: String): Unit = {
    printf("\n%50s          Cosine\n------------------------------------------------------------------------\n", query)
  }

  @throws[IOException]
  def trainOrLoadModel: WordVectorModel = {
    if (!IOUtil.isFileExisted(MODEL_FILE_NAME)) {
      if (!IOUtil.isFileExisted(TRAIN_FILE_NAME)) {
        System.err.println("语料不存在，请阅读文档了解语料获取与格式：https://github.com/hankcs/HanLP/wiki/word2vec")
        System.exit(1)
      }
      val trainerBuilder = new Word2VecTrainer
      return trainerBuilder.train(TRAIN_FILE_NAME, MODEL_FILE_NAME)
    }
    loadModel
  }

  @throws[IOException]
  def loadModel = new WordVectorModel(MODEL_FILE_NAME)
}

