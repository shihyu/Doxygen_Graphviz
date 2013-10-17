#pragma once

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace singleton
			{
				namespace example5
				{

					class Singleton
					{
						//4������һ���������洢�����õ���ʵ��
						//5����Ϊ�������Ҫ�ھ�̬������ʹ�ã�������Ҫ����static����
					private:
						static Singleton instance;
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