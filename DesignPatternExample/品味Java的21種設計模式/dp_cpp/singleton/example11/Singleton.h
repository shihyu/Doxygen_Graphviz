#pragma once

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace singleton
			{
				namespace example11
				{


					class Singleton
					{
					///	
					///	 <summary> * �༶���ڲ��࣬Ҳ���Ǿ�̬�ĳ�Աʽ�ڲ��࣬���ڲ����ʵ�����ⲿ���ʵ��û�а󶨹�ϵ��
					///	 * ����ֻ�б����õ��Ż�װ�أ��Ӷ�ʵ�����ӳټ��� </summary>
					///	 
					private:
						class SingletonHolder
						{
					///		
					///		 <summary> * ��̬��ʼ��������JVM����֤�̰߳�ȫ </summary>
					///		 
						private:
//JAVA TO C++ CONVERTER TODO TASK: Native C++ only allows initialization of static const integral fields in their declarations:
							static Singleton instance = new Singleton();
						};
					///	
					///	 <summary> * ˽�л����췽�� </summary>
					///	 
					private:
						Singleton();

					public:
						static Singleton getInstance();
					};
				}
			}
		}
	}
}