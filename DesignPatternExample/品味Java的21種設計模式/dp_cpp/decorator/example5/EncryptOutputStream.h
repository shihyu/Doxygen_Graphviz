#pragma once

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace decorator
			{
				namespace example5
				{


					///
					/// <summary> * ʵ�ּ򵥵ļ��� </summary>
					/// 
					class EncryptOutputStream : public OutputStream
					{
						//���б�װ�εĶ���
					private:
						OutputStream *os;
					public:
						EncryptOutputStream(OutputStream *os);

						virtual void write(int a) throw(IOException);
					};

				}
			}
		}
	}
}