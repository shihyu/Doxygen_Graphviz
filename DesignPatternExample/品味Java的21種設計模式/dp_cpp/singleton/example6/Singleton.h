#pragma once

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace singleton
			{
				namespace example6
				{

					class Singleton
					{
						//4������һ����̬�������洢�����õ���ʵ��
						//ֱ�������ﴴ����ʵ����ֻ�ᴴ��һ��
					private:
//JAVA TO C++ CONVERTER TODO TASK: Native C++ only allows initialization of static const integral fields in their declarations:
						static Singleton instance = new Singleton();
						//1��˽�л����췽���������ڲ����ƴ���ʵ������Ŀ
						Singleton();
						//2������һ��������Ϊ�ͻ����ṩ��ʵ��
						//3�����������Ҫ������෽����Ҳ����Ҫ��static
					public:
						static Singleton getInstance();
					};

				}
			}
		}
	}
}